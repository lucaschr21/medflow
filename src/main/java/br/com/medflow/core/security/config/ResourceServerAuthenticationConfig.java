package br.com.medflow.core.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;

import br.com.medflow.core.security.authentication.AuthenticatedUserMapper;
import br.com.medflow.core.security.authentication.MedflowOpaqueTokenIntrospector;
import br.com.medflow.core.security.authorization.TokenAuthoritiesMapper;

/**
 * Configuração de autenticação do backend como OAuth2 resource server.
 *
 * <p>Este módulo configura a introspecção de bearer token no Keycloak e os
 * beans auxiliares usados pela autenticação.
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties({AuthenticationProperties.class, AuthorizationProperties.class})
public class ResourceServerAuthenticationConfig {

  /**
   * Publica o builder HTTP usado pelas integrações de segurança.
   *
   * @return builder HTTP do Spring
   */
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  RestClient.Builder restClientBuilder() {
    return RestClient.builder();
  }

  /**
   * Cria o introspector de token usado pelo resource server.
   *
   * @param properties propriedades de introspecção configuradas para o resource
   *        server
   * @param authenticatedUserMapper mapper da identidade autenticada
   * @param tokenAuthoritiesMapper mapper das authorities locais
   * @return introspector configurado
   */
  @Bean
  OpaqueTokenIntrospector opaqueTokenIntrospector(
      AuthenticationProperties properties,
      AuthenticatedUserMapper authenticatedUserMapper,
      TokenAuthoritiesMapper tokenAuthoritiesMapper) {
    OpaqueTokenIntrospector delegate = SpringOpaqueTokenIntrospector
        .withIntrospectionUri(properties.introspectionUri())
        .clientId(properties.clientId())
        .clientSecret(properties.clientSecret())
        .build();
    return new MedflowOpaqueTokenIntrospector(
        delegate, authenticatedUserMapper, tokenAuthoritiesMapper);
  }
}
