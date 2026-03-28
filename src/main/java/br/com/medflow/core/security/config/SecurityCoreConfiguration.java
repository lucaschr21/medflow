package br.com.medflow.core.security.config;

import java.time.Clock;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import br.com.medflow.core.security.infrastructure.audit.Slf4jSecurityAuditAdapter;
import br.com.medflow.core.security.infrastructure.jwt.SpringJwtTokenVerifierAdapter;
import br.com.medflow.core.security.infrastructure.revocation.JpaTokenRevocationAdapter;
import br.com.medflow.repositories.seguranca.TokenRevogadoRepository;
import br.com.medflow.services.seguranca.AuthorizationPolicy;
import br.com.medflow.services.seguranca.DefaultAuthorizationPolicy;
import br.com.medflow.services.seguranca.SecurityAuthorizationService;
import br.com.medflow.services.seguranca.ports.SecurityAuditPort;
import br.com.medflow.services.seguranca.ports.TokenRevocationPort;
import br.com.medflow.services.seguranca.ports.TokenVerifierPort;

@Configuration
@ConditionalOnProperty(name = "medflow.security.oauth2.enabled", havingValue = "true")
@EnableConfigurationProperties(SecurityOAuth2Properties.class)
public class SecurityCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean
  Clock securityClock() {
    return Clock.systemUTC();
  }

  @Bean
  @ConditionalOnMissingBean
  AuthorizationPolicy authorizationPolicy() {
    return new DefaultAuthorizationPolicy();
  }

  @Bean
  @ConditionalOnMissingBean(JwtDecoder.class)
  JwtDecoder jwtDecoder(SecurityOAuth2Properties properties) {
    String issuerUri = properties.effectiveIssuerUri();

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    decoder.setJwtValidator(jwtTokenValidator(properties));
    return decoder;
  }

  private static OAuth2TokenValidator<Jwt> jwtTokenValidator(SecurityOAuth2Properties properties) {
    String issuerUri = properties.effectiveIssuerUri();
    String audience = properties.effectiveAudience();

    if (audience == null) {
      return new DelegatingOAuth2TokenValidator<>(
          JwtValidators.createDefaultWithIssuer(issuerUri),
          new JwtTimestampValidator(properties.effectiveClockSkew()));
    }

    JwtClaimValidator<List<String>> audienceValidator =
        new JwtClaimValidator<>("aud", aud -> aud != null && aud.contains(audience));

    return new DelegatingOAuth2TokenValidator<>(
        JwtValidators.createDefaultWithIssuer(issuerUri),
        new JwtTimestampValidator(properties.effectiveClockSkew()),
        audienceValidator);
  }

  @Bean
  @ConditionalOnMissingBean(TokenVerifierPort.class)
  TokenVerifierPort tokenVerifierPort(JwtDecoder jwtDecoder) {
    return new SpringJwtTokenVerifierAdapter(jwtDecoder);
  }

  @Bean
  @ConditionalOnMissingBean(JpaTokenRevocationAdapter.class)
  JpaTokenRevocationAdapter jpaTokenRevocationAdapter(
      TokenRevogadoRepository tokenRevogadoRepository) {
    return new JpaTokenRevocationAdapter(tokenRevogadoRepository);
  }

  @Bean
  @ConditionalOnMissingBean(TokenRevocationPort.class)
  TokenRevocationPort tokenRevocationPort(JpaTokenRevocationAdapter revocationAdapter) {
    return revocationAdapter;
  }

  @Bean
  @ConditionalOnMissingBean(SecurityAuditPort.class)
  SecurityAuditPort securityAuditPort() {
    return new Slf4jSecurityAuditAdapter();
  }

  @Bean
  @ConditionalOnMissingBean(SecurityAuthorizationService.class)
  SecurityAuthorizationService securityAuthorizationService(
      TokenVerifierPort tokenVerifierPort,
      TokenRevocationPort tokenRevocationPort,
      AuthorizationPolicy authorizationPolicy,
      SecurityAuditPort securityAuditPort,
      Clock securityClock) {
    return new SecurityAuthorizationService(
        tokenVerifierPort,
        tokenRevocationPort,
        authorizationPolicy,
        securityAuditPort,
        securityClock);
  }
}
