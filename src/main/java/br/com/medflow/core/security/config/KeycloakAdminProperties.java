package br.com.medflow.core.security.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Role;

/**
 * Propriedades para integração com a API administrativa do Keycloak.
 *
 * @param baseUrl      URL base do Keycloak (ex: http://localhost:8085)
 * @param realm        nome do realm
 * @param clientId     client técnico com service account para operações admin
 * @param clientSecret secret do client técnico
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConfigurationProperties(prefix = "medflow.keycloak.admin")
public record KeycloakAdminProperties(
                String baseUrl,
                String realm,
                String clientId,
                String clientSecret) {
}
