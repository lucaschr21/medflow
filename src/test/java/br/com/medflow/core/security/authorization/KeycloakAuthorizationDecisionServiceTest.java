package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withForbiddenRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.medflow.core.security.config.AuthorizationProperties;
import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

class KeycloakAuthorizationDecisionServiceTest {

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldReturnGrantedWhenKeycloakApprovesPermission() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KeycloakAuthorizationDecisionService service = new KeycloakAuthorizationDecisionService(
                restClientBuilder,
                new AuthorizationProperties(
                        "http://localhost:8085/realms/medflow/protocol/openid-connect/token",
                        "medflow-backend"),
                new RequestAuthorizationDecisionCache());

        server
                .expect(
                        once(), requestTo("http://localhost:8085/realms/medflow/protocol/openid-connect/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer access-token-value"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(
                        content()
                                .string(
                                        org.hamcrest.Matchers.allOf(
                                                org.hamcrest.Matchers.containsString(
                                                        "grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Auma-ticket"),
                                                org.hamcrest.Matchers.containsString("audience=medflow-backend"),
                                                org.hamcrest.Matchers.containsString("permission=usuario%23read"),
                                                org.hamcrest.Matchers.containsString("response_mode=decision"))))
                .andRespond(withSuccess("{\"result\":true}", MediaType.APPLICATION_JSON));

        boolean granted = service.isGranted(
                authentication(), FunctionalPermission.of("usuario", ResourceAction.READ));

        assertTrue(granted);
        server.verify();
    }

    @Test
    void shouldReturnDeniedWhenKeycloakRejectsPermission() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KeycloakAuthorizationDecisionService service = new KeycloakAuthorizationDecisionService(
                restClientBuilder,
                new AuthorizationProperties(
                        "http://localhost:8085/realms/medflow/protocol/openid-connect/token",
                        "medflow-backend"),
                new RequestAuthorizationDecisionCache());

        server
                .expect(
                        once(), requestTo("http://localhost:8085/realms/medflow/protocol/openid-connect/token"))
                .andRespond(withForbiddenRequest());

        boolean granted = service.isGranted(
                authentication(), FunctionalPermission.of("usuario", ResourceAction.DELETE));

        assertFalse(granted);
        server.verify();
    }

    @Test
    void shouldCacheDecisionDuringCurrentRequest() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KeycloakAuthorizationDecisionService service = new KeycloakAuthorizationDecisionService(
                restClientBuilder,
                new AuthorizationProperties(
                        "http://localhost:8085/realms/medflow/protocol/openid-connect/token",
                        "medflow-backend"),
                new RequestAuthorizationDecisionCache());

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest("GET", "/api/usuarios")));

        server
                .expect(
                        once(), requestTo("http://localhost:8085/realms/medflow/protocol/openid-connect/token"))
                .andRespond(withSuccess("{\"result\":true}", MediaType.APPLICATION_JSON));

        FunctionalPermission permission = FunctionalPermission.of("usuario", ResourceAction.READ);

        assertTrue(service.isGranted(authentication(), permission));
        assertTrue(service.isGranted(authentication(), permission));
        server.verify();
    }

    @Test
    void shouldNotReuseDecisionForDifferentBearerTokenInSameRequest() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KeycloakAuthorizationDecisionService service = new KeycloakAuthorizationDecisionService(
                restClientBuilder,
                new AuthorizationProperties(
                        "http://localhost:8085/realms/medflow/protocol/openid-connect/token",
                        "medflow-backend"),
                new RequestAuthorizationDecisionCache());

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest("GET", "/api/usuarios")));

        server
                .expect(
                        once(), requestTo("http://localhost:8085/realms/medflow/protocol/openid-connect/token"))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer access-token-value"))
                .andRespond(withSuccess("{\"result\":true}", MediaType.APPLICATION_JSON));

        server
                .expect(
                        once(), requestTo("http://localhost:8085/realms/medflow/protocol/openid-connect/token"))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer another-access-token"))
                .andRespond(withSuccess("{\"result\":true}", MediaType.APPLICATION_JSON));

        FunctionalPermission permission = FunctionalPermission.of("usuario", ResourceAction.READ);

        assertTrue(service.isGranted(authentication("access-token-value"), permission));
        assertTrue(service.isGranted(authentication("another-access-token"), permission));
        server.verify();
    }

    @Test
    void shouldFailClosedWhenDecisionPayloadDoesNotContainBooleanResult() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KeycloakAuthorizationDecisionService service = new KeycloakAuthorizationDecisionService(
                restClientBuilder,
                new AuthorizationProperties(
                        "http://localhost:8085/realms/medflow/protocol/openid-connect/token",
                        "medflow-backend"),
                new RequestAuthorizationDecisionCache());

        server
                .expect(
                        once(), requestTo("http://localhost:8085/realms/medflow/protocol/openid-connect/token"))
                .andRespond(withSuccess("{\"result\":\"true\"}", MediaType.APPLICATION_JSON));

        assertThrows(
                AuthorizationServiceException.class,
                () -> service.isGranted(
                        authentication(), FunctionalPermission.of("usuario", ResourceAction.READ)));
        server.verify();
    }

    private static BearerTokenAuthentication authentication() {
        return authentication("access-token-value");
    }

    private static BearerTokenAuthentication authentication(String tokenValue) {
        MedflowAuthenticatedPrincipal principal = new MedflowAuthenticatedPrincipal(
                new AuthenticatedUser(
                        "keycloak-user-id",
                        "jane.doe",
                        "jane@medflow.com",
                        "Jane Doe",
                        "12345678901",
                        "91999999999",
                        LocalDate.parse("1990-04-10"),
                        java.util.Set.of("default-roles-medflow"),
                        Map.of("medflow-backend", java.util.Set.of("MEDICO")),
                        java.util.Set.of("MEDICOS")),
                Map.of(),
                List.of());

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                Instant.now(),
                Instant.now().plusSeconds(300));

        return new BearerTokenAuthentication(principal, accessToken, principal.getAuthorities());
    }
}
