package br.com.medflow.core.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import br.com.medflow.core.security.authentication.AuthenticatedUserMapper;
import br.com.medflow.core.security.authentication.MedflowOpaqueTokenIntrospector;
import br.com.medflow.core.security.authorization.PermissionAuthoritiesMapper;

/**
 * Configuração de autenticação do backend como OAuth2 resource server.
 *
 * <p>Este módulo registra a {@link SecurityFilterChain} stateless da aplicação
 * e configura a introspecção de bearer token no Keycloak.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthenticationProperties.class)
public class ResourceServerAuthenticationConfig {

  /**
   * Configura o backend como resource server stateless.
   *
   * @param http configurador de segurança HTTP
   * @return filter chain configurada
   * @throws Exception quando houver falha de configuração
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(
                    "/api/actuator/health",
                    "/api/actuator/info",
                    "/api/openapi",
                    "/api/openapi/**",
                    "/docs",
                    "/docs/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()));

    return http.build();
  }

  /**
   * Cria o introspector de token usado pelo resource server.
   *
   * @param properties propriedades de introspecção configuradas para o resource
   *        server
   * @param authenticatedUserMapper mapper da identidade autenticada
   * @param permissionAuthoritiesMapper mapper das authorities funcionais
   * @return introspector configurado
   */
  @Bean
  OpaqueTokenIntrospector opaqueTokenIntrospector(
      AuthenticationProperties properties,
      AuthenticatedUserMapper authenticatedUserMapper,
      PermissionAuthoritiesMapper permissionAuthoritiesMapper) {
    OpaqueTokenIntrospector delegate = SpringOpaqueTokenIntrospector
        .withIntrospectionUri(properties.introspectionUri())
        .clientId(properties.clientId())
        .clientSecret(properties.clientSecret())
        .build();
    return new MedflowOpaqueTokenIntrospector(
        delegate, authenticatedUserMapper, permissionAuthoritiesMapper);
  }
}
