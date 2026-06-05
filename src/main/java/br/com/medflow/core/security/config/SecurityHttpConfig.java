package br.com.medflow.core.security.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import br.com.medflow.core.logging.RequestLoggingFilter;

/**
 * Configura a cadeia HTTP de segurança da aplicação.
 *
 * <p>Esta configuração concentra regras transversais da borda web, como sessão
 * stateless, rotas públicas e filtros de infraestrutura.
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SecurityHttpConfig {

  /**
   * Configura o backend como resource server stateless.
   *
   * @param http configurador de segurança HTTP
   * @param requestLoggingFilter filtro de logging das requisições HTTP
   * @return filter chain configurada
   * @throws Exception quando houver falha de configuração
   */
  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      RequestLoggingFilter requestLoggingFilter) throws Exception {
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
        .addFilterAfter(requestLoggingFilter, AnonymousAuthenticationFilter.class)
        .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()));

    return http.build();
  }
}
