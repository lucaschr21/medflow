package br.com.medflow.core.security.infrastructure.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.medflow.core.security.AuthorizationRequest;
import br.com.medflow.core.security.AuthorizationResult;
import br.com.medflow.core.security.TokenClaims;
import br.com.medflow.services.seguranca.ports.SecurityAuditPort;

public class Slf4jSecurityAuditAdapter implements SecurityAuditPort {

  private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jSecurityAuditAdapter.class);

  @Override
  public void onAuthorizationGranted(TokenClaims tokenClaims, AuthorizationRequest request) {
    if (!LOGGER.isDebugEnabled()) {
      return;
    }

    LOGGER.debug(
        "Authorization granted: sub={}, tokenRef={}, acceptedAudiences={}, requiredScopes={}",
        safeSubject(tokenClaims),
        maskedTokenId(tokenClaims),
        request.acceptedAudiences(),
        request.requiredScopes());
  }

  @Override
  public void onAuthorizationDenied(
      TokenClaims tokenClaims, AuthorizationRequest request, AuthorizationResult result) {
    if (!LOGGER.isWarnEnabled()) {
      return;
    }

    LOGGER.warn(
        "Authorization denied: sub={}, tokenRef={}, errorCode={}, reason={}, acceptedAudiences={}, requiredScopes={}",
        safeSubject(tokenClaims),
        maskedTokenId(tokenClaims),
        result.errorCode(),
        result.reason(),
        request.acceptedAudiences(),
        request.requiredScopes());
  }

  private static String safeSubject(TokenClaims tokenClaims) {
    if (tokenClaims == null || tokenClaims.subject() == null || tokenClaims.subject().isBlank()) {
      return "unknown";
    }
    return tokenClaims.subject();
  }

  private static String maskedTokenId(TokenClaims tokenClaims) {
    if (tokenClaims == null || tokenClaims.tokenId() == null || tokenClaims.tokenId().isBlank()) {
      return "unknown";
    }

    String tokenId = tokenClaims.tokenId();
    int visibleChars = Math.min(8, tokenId.length());
    return tokenId.substring(0, visibleChars) + "...";
  }
}
