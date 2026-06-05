package br.com.medflow.core.security.authorization;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

import br.com.medflow.core.security.config.AuthorizationProperties;

/**
 * Consulta o Keycloak Authorization Services para decidir permissões
 * funcionais do backend.
 *
 * <p>O backend envia o access token do usuário ao token endpoint com o grant
 * UMA e solicita a decisão para um {@code recurso#escopo} específico.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class KeycloakAuthorizationDecisionService implements FunctionalPermissionDecisionService {

  private static final String UMA_TICKET_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:uma-ticket";
  private static final String DECISION_RESPONSE_MODE = "decision";
  private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

  private final RestClient restClient;
  private final AuthorizationProperties authorizationProperties;
  private final RequestAuthorizationDecisionCache requestAuthorizationDecisionCache;

  /**
   * Cria o serviço de decisão funcional do Keycloak.
   *
   * @param restClientBuilder builder HTTP do Spring
   * @param authorizationProperties propriedades da autorização funcional
   * @param requestAuthorizationDecisionCache cache por requisição
   */
  public KeycloakAuthorizationDecisionService(
      RestClient.Builder restClientBuilder,
      AuthorizationProperties authorizationProperties,
      RequestAuthorizationDecisionCache requestAuthorizationDecisionCache) {
    this.restClient = restClientBuilder.build();
    this.authorizationProperties = Objects.requireNonNull(authorizationProperties);
    this.requestAuthorizationDecisionCache = Objects.requireNonNull(requestAuthorizationDecisionCache);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isGranted(Authentication authentication, FunctionalPermission permission) {
    String accessToken = accessToken(authentication);
    return requestAuthorizationDecisionCache.getOrCompute(
        accessToken,
        permission,
        () -> requestDecision(accessToken, permission));
  }

  private boolean requestDecision(String accessToken, FunctionalPermission permission) {
    return restClient.post()
        .uri(authorizationProperties.tokenUri())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .body(requestBody(permission))
        .exchange((request, response) -> {
          HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
          if (status == HttpStatus.FORBIDDEN) {
            return false;
          }
          if (!status.is2xxSuccessful()) {
            throw new AuthorizationServiceException(
                "Falha ao consultar a autorização funcional no Keycloak. Status HTTP: " + status.value());
          }

          return AuthorizationDecisionResponse.parse(response.bodyTo(byte[].class)).result();
        });
  }

  private MultiValueMap<String, String> requestBody(FunctionalPermission permission) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(4);
    parameters.add("grant_type", UMA_TICKET_GRANT_TYPE);
    parameters.add("audience", authorizationProperties.audience());
    parameters.add("permission", permission.resource() + "#" + permission.action().value());
    parameters.add("response_mode", DECISION_RESPONSE_MODE);
    return parameters;
  }

  private static String accessToken(Authentication authentication) {
    if (authentication instanceof BearerTokenAuthentication bearerTokenAuthentication) {
      return bearerTokenAuthentication.getToken().getTokenValue();
    }

    throw new AuthorizationServiceException("A autenticação atual não expõe um bearer token");
  }

  private record AuthorizationDecisionResponse(boolean result) {

    private static AuthorizationDecisionResponse parse(byte[] responseBody) {
      try {
        JsonNode payload = JSON_MAPPER.readTree(responseBody);
        JsonNode result = payload.get("result");
        if (result == null || !result.isBoolean()) {
          throw new AuthorizationServiceException(
              "A resposta de autorização do Keycloak não contém um campo booleano 'result'");
        }
        return new AuthorizationDecisionResponse(result.booleanValue());
      } catch (AuthorizationServiceException exception) {
        throw exception;
      } catch (IOException exception) {
        throw new AuthorizationServiceException(
            "Falha ao ler a resposta de autorização funcional do Keycloak", exception);
      }
    }
  }
}
