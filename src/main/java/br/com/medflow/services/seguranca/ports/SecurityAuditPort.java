package br.com.medflow.services.seguranca.ports;

import br.com.medflow.core.security.AuthorizationRequest;
import br.com.medflow.core.security.AuthorizationResult;
import br.com.medflow.core.security.TokenClaims;

public interface SecurityAuditPort {

  void onAuthorizationGranted(TokenClaims tokenClaims, AuthorizationRequest request);

  void onAuthorizationDenied(
      TokenClaims tokenClaims, AuthorizationRequest request, AuthorizationResult result);
}
