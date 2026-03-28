package br.com.medflow.core.security;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public record TokenClaims(
    String tokenId,
    String issuer,
    String subject,
    Set<String> audiences,
    Set<String> scopes,
    Instant issuedAt,
    Instant notBefore,
    Instant expiresAt) {

  public TokenClaims {
    tokenId = requireNonBlank(tokenId, "JTI do token é obrigatório");
    issuer = requireNonBlank(issuer, "Issuer do token é obrigatório");
    subject = requireNonBlank(subject, "Subject do token é obrigatório");
    audiences = SecurityStringSetNormalizer.normalize(audiences);
    scopes = SecurityStringSetNormalizer.normalize(scopes);
    issuedAt = Objects.requireNonNull(issuedAt, "Issued at do token é obrigatório");
    expiresAt = Objects.requireNonNull(expiresAt, "Expiração do token é obrigatória");

    if (audiences.isEmpty()) {
      throw new IllegalArgumentException("Audience do token é obrigatória");
    }
    if (!expiresAt.isAfter(issuedAt)) {
      throw new IllegalArgumentException("Expiração deve ser posterior ao issued at");
    }
    if (notBefore != null && expiresAt.isBefore(notBefore)) {
      throw new IllegalArgumentException("Expiração não pode ser anterior ao not before");
    }
  }

  public boolean isExpired(Instant now) {
    Objects.requireNonNull(now, "Instante atual é obrigatório");
    return !expiresAt.isAfter(now);
  }

  public boolean isNotYetValid(Instant now) {
    Objects.requireNonNull(now, "Instante atual é obrigatório");
    return notBefore != null && now.isBefore(notBefore);
  }

  public boolean hasAllScopes(Set<String> requiredScopes) {
    if (requiredScopes == null || requiredScopes.isEmpty()) {
      return true;
    }
    return scopes.containsAll(SecurityStringSetNormalizer.normalize(requiredScopes));
  }

  public boolean hasAnyAudience(Set<String> expectedAudiences) {
    if (expectedAudiences == null || expectedAudiences.isEmpty()) {
      return true;
    }

    Set<String> normalizedExpectedAudiences = SecurityStringSetNormalizer.normalize(expectedAudiences);
    if (normalizedExpectedAudiences.isEmpty()) {
      return true;
    }
    return audiences.stream().anyMatch(normalizedExpectedAudiences::contains);
  }

  private static String requireNonBlank(String value, String message) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    return value.strip();
  }
}
