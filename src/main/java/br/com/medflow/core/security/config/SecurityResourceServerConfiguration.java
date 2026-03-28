package br.com.medflow.core.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityResourceServerConfiguration {

  @Bean
  @ConditionalOnProperty(name = "medflow.security.oauth2.enabled", havingValue = "true")
  SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/oauth2/revoke")
                    .access(OAuth2AuthorizationManagers.hasScope("token.revoke"))
                    .anyRequest()
            .permitAll())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    return http.build();
  }

  @Bean
  @ConditionalOnProperty(
      name = "medflow.security.oauth2.enabled",
      havingValue = "false",
      matchIfMissing = true)
  SecurityFilterChain openSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

    return http.build();
  }
}
