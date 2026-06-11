package br.com.medflow.core.security.identity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.medflow.core.security.config.KeycloakAdminProperties;

/**
 * Implementação da porta {@link IdentityProviderAdminPort} usando a
 * API REST administrativa do Keycloak.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class KeycloakIdentityProviderAdminAdapter implements IdentityProviderAdminPort {

        private static final String CLIENT_CREDENTIALS = "client_credentials";

        private final RestClient restClient;
        private final KeycloakAdminProperties properties;

        public KeycloakIdentityProviderAdminAdapter(
                        RestClient.Builder restClientBuilder,
                        KeycloakAdminProperties properties) {
                this.restClient = restClientBuilder.build();
                this.properties = properties;
        }

        @Override
        public String criarUsuario(String username, String email, String firstName,
                        String lastName, Set<String> groups) {
                String adminToken = obterAdminToken();

                var response = restClient.post()
                                .uri("{baseUrl}/admin/realms/{realm}/users",
                                                properties.baseUrl(), properties.realm())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .body(Map.of(
                                                "username", username,
                                                "email", email,
                                                "firstName", firstName,
                                                "lastName", lastName,
                                                "enabled", true,
                                                "emailVerified", true))
                                .retrieve()
                                .toBodilessEntity();

                var location = response.getHeaders().getLocation();
                if (location == null) {
                        throw new IllegalStateException("Keycloak não retornou o Location do usuário criado.");
                }

                String keycloakId = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);

                for (String group : groups) {
                        UUID groupId = buscarOuCriarGrupo(adminToken, group);
                        adicionarUsuarioAoGrupo(adminToken, keycloakId, groupId);
                }

                return keycloakId;
        }

        @Override
        public void atualizarUsuario(String keycloakId, String username, String email,
                        String firstName, String lastName) {
                String adminToken = obterAdminToken();

                restClient.put()
                                .uri("{baseUrl}/admin/realms/{realm}/users/{id}",
                                                properties.baseUrl(), properties.realm(), keycloakId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .body(Map.of(
                                                "username", username,
                                                "email", email,
                                                "firstName", firstName,
                                                "lastName", lastName))
                                .retrieve()
                                .toBodilessEntity();
        }

        @Override
        public void desabilitarUsuario(String keycloakId) {
                String adminToken = obterAdminToken();

                restClient.put()
                                .uri("{baseUrl}/admin/realms/{realm}/users/{id}",
                                                properties.baseUrl(), properties.realm(), keycloakId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .body(Map.of("enabled", false))
                                .retrieve()
                                .toBodilessEntity();
        }

        @Override
        public void habilitarUsuario(String keycloakId) {
                String adminToken = obterAdminToken();

                restClient.put()
                                .uri("{baseUrl}/admin/realms/{realm}/users/{id}",
                                                properties.baseUrl(), properties.realm(), keycloakId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .body(Map.of("enabled", true))
                                .retrieve()
                                .toBodilessEntity();
        }

        private String obterAdminToken() {
                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("grant_type", CLIENT_CREDENTIALS);
                body.add("client_id", properties.clientId());
                body.add("client_secret", properties.clientSecret());

                JsonNode response = restClient.post()
                                .uri("{baseUrl}/realms/{realm}/protocol/openid-connect/token",
                                                properties.baseUrl(), properties.realm())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(body)
                                .retrieve()
                                .body(JsonNode.class);

                return response.get("access_token").asText();
        }

        private UUID buscarOuCriarGrupo(String adminToken, String groupName) {
                JsonNode groups = restClient.get()
                                .uri("{baseUrl}/admin/realms/{realm}/groups?search={name}&exact=true",
                                                properties.baseUrl(), properties.realm(), groupName)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .retrieve()
                                .body(JsonNode.class);

                if (groups != null && groups.isArray() && !groups.isEmpty()) {
                        return UUID.fromString(groups.get(0).get("id").asText());
                }

                var response = restClient.post()
                                .uri("{baseUrl}/admin/realms/{realm}/groups",
                                                properties.baseUrl(), properties.realm())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .body(Map.of("name", groupName))
                                .retrieve()
                                .toBodilessEntity();

                var location = response.getHeaders().getLocation();
                if (location == null) {
                        throw new IllegalStateException("Keycloak não retornou o Location do grupo criado.");
                }
                return UUID.fromString(location.getPath().substring(location.getPath().lastIndexOf('/') + 1));
        }

        private void adicionarUsuarioAoGrupo(String adminToken, String keycloakId, UUID groupId) {
                restClient.put()
                                .uri("{baseUrl}/admin/realms/{realm}/users/{userId}/groups/{groupId}",
                                                properties.baseUrl(), properties.realm(), keycloakId, groupId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                                .retrieve()
                                .toBodilessEntity();
        }
}
