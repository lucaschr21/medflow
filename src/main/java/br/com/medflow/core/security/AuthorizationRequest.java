package br.com.medflow.core.security;

import java.util.Set;

public record AuthorizationRequest(Set<String> requiredScopes, Set<String> acceptedAudiences) {

  public AuthorizationRequest {
    requiredScopes = SecurityStringSetNormalizer.normalize(requiredScopes);
    acceptedAudiences = SecurityStringSetNormalizer.normalize(acceptedAudiences);
  }
}
