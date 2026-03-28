package br.com.medflow.services.seguranca;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import br.com.medflow.core.security.AuthorizationRequest;
import br.com.medflow.core.security.AuthorizationResult;
import br.com.medflow.core.security.SecurityErrorCode;
import br.com.medflow.core.security.TokenClaims;
import br.com.medflow.services.seguranca.ports.SecurityAuditPort;
import br.com.medflow.services.seguranca.ports.TokenRevocationPort;
import br.com.medflow.services.seguranca.ports.TokenVerifierPort;

public class SecurityAuthorizationService {

  private final TokenVerifierPort tokenVerifierPort;
  private final TokenRevocationPort tokenRevocationPort;
  private final AuthorizationPolicy authorizationPolicy;
  private final SecurityAuditPort securityAuditPort;
  private final Clock clock;

  public SecurityAuthorizationService(
      TokenVerifierPort tokenVerifierPort,
      TokenRevocationPort tokenRevocationPort,
      AuthorizationPolicy authorizationPolicy,
      SecurityAuditPort securityAuditPort,
      Clock clock) {
    this.tokenVerifierPort =
      Objects.requireNonNull(tokenVerifierPort, "Verificador de token é obrigatório");
    this.tokenRevocationPort =
      Objects.requireNonNull(tokenRevocationPort, "Porta de revogação é obrigatória");
    this.authorizationPolicy =
      Objects.requireNonNull(authorizationPolicy, "Política de autorização é obrigatória");
    this.securityAuditPort =
      Objects.requireNonNull(securityAuditPort, "Porta de auditoria é obrigatória");
    this.clock = Objects.requireNonNull(clock, "Clock é obrigatório");
  }

  public AuthorizationResult authorize(String accessToken, AuthorizationRequest request) {
    Objects.requireNonNull(request, "Requisição de autorização é obrigatória");
    if (accessToken == null || accessToken.isBlank()) {
      return denyAndAudit(
          null,
          request,
          SecurityErrorCode.INVALID_TOKEN,
          "Token inválido ou malformado");
    }

    TokenClaims tokenClaims;
    try {
      tokenClaims = tokenVerifierPort.verifyAccessToken(accessToken);
    } catch (RuntimeException ex) {
      return denyAndAudit(
          null,
          request,
          SecurityErrorCode.INVALID_TOKEN,
          "Token inválido ou malformado");
    }

    if (tokenRevocationPort.isRevoked(tokenClaims.tokenId())) {
      return denyAndAudit(tokenClaims, request, SecurityErrorCode.TOKEN_REVOKED, "Token revogado");
    }

    Instant now = Instant.now(clock);
    AuthorizationResult result = authorizationPolicy.evaluate(tokenClaims, request, now);

    if (result.granted()) {
      securityAuditPort.onAuthorizationGranted(tokenClaims, request);
      return result;
    }

    securityAuditPort.onAuthorizationDenied(tokenClaims, request, result);
    return result;
  }

  private AuthorizationResult denyAndAudit(
      TokenClaims tokenClaims,
      AuthorizationRequest request,
      SecurityErrorCode errorCode,
      String reason) {
    AuthorizationResult denied = AuthorizationResult.deny(errorCode, reason);
    securityAuditPort.onAuthorizationDenied(tokenClaims, request, denied);
    return denied;
  }
}
