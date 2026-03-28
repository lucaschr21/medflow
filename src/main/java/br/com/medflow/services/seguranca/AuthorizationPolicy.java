package br.com.medflow.services.seguranca;

import java.time.Instant;

import br.com.medflow.core.security.AuthorizationRequest;
import br.com.medflow.core.security.AuthorizationResult;
import br.com.medflow.core.security.TokenClaims;

public interface AuthorizationPolicy {

  AuthorizationResult evaluate(TokenClaims tokenClaims, AuthorizationRequest request, Instant now);
}
