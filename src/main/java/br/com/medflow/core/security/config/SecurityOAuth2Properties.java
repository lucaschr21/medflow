package br.com.medflow.core.security.config;

import java.time.Duration;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "medflow.security.oauth2")
public record SecurityOAuth2Properties(
    boolean enabled,
    String issuerUri,
    String audience,
    Duration clockSkew) {

  public String effectiveIssuerUri() {
    String normalizedIssuerUri =
        Objects.requireNonNull(issuerUri, "Issuer URI é obrigatório").trim();
    if (normalizedIssuerUri.isBlank()) {
      throw new IllegalStateException("Issuer URI é obrigatório");
    }
    return normalizedIssuerUri;
  }

  public String effectiveAudience() {
    if (audience == null) {
      return null;
    }
    String normalizedAudience = audience.trim();
    return normalizedAudience.isBlank() ? null : normalizedAudience;
  }

  public Duration effectiveClockSkew() {
    Duration configuredClockSkew = clockSkew == null ? Duration.ofSeconds(60) : clockSkew;
    if (configuredClockSkew.isNegative()) {
      throw new IllegalStateException("Clock skew não pode ser negativo");
    }
    return configuredClockSkew;
  }
}
