package br.com.medflow.services.seguranca;

import java.time.Instant;
import java.util.Objects;

import br.com.medflow.core.security.AuthorizationRequest;
import br.com.medflow.core.security.AuthorizationResult;
import br.com.medflow.core.security.SecurityErrorCode;
import br.com.medflow.core.security.TokenClaims;

public class DefaultAuthorizationPolicy implements AuthorizationPolicy {

  @Override
  public AuthorizationResult evaluate(TokenClaims tokenClaims, AuthorizationRequest request, Instant now) {
    Objects.requireNonNull(tokenClaims, "Token é obrigatório");
    Objects.requireNonNull(request, "Requisição de autorização é obrigatória");
    Objects.requireNonNull(now, "Instante de validação é obrigatório");

    if (tokenClaims.isNotYetValid(now)) {
      return AuthorizationResult.deny(
          SecurityErrorCode.TOKEN_NOT_YET_VALID, "Token ainda não está válido");
    }

    if (tokenClaims.isExpired(now)) {
      return AuthorizationResult.deny(SecurityErrorCode.TOKEN_EXPIRED, "Token expirado");
    }

    if (!tokenClaims.hasAllScopes(request.requiredScopes())) {
      return AuthorizationResult.deny(
          SecurityErrorCode.INSUFFICIENT_SCOPE,
          "Escopo insuficiente para acessar o recurso solicitado");
    }

    if (!tokenClaims.hasAnyAudience(request.acceptedAudiences())) {
      return AuthorizationResult.deny(
          SecurityErrorCode.INVALID_AUDIENCE,
          "Audience inválida para este recurso");
    }

    return AuthorizationResult.allow();
  }
}
