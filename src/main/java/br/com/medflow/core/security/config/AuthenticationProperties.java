package br.com.medflow.core.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;

/**
 * Propriedades de autenticação do resource server do Medflow.
 *
 * @param introspectionUri endpoint de introspecção do authorization server
 * @param clientId identificador do client usado na introspecção
 * @param clientSecret segredo do client usado na introspecção
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.opaquetoken")
public record AuthenticationProperties(
    String introspectionUri,
    String clientId,
    String clientSecret) {}
