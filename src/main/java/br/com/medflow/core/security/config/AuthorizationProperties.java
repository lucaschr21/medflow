package br.com.medflow.core.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;

/**
 * Propriedades da autorização funcional integrada ao Keycloak.
 *
 * @param tokenUri endpoint de token usado para solicitar decisões de autorização
 * @param audience client do resource server avaliado pelo Authorization Services
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.authorization")
public record AuthorizationProperties(
    String tokenUri,
    String audience) {}
