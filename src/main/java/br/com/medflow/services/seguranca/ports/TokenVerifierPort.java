package br.com.medflow.services.seguranca.ports;

import br.com.medflow.core.security.TokenClaims;

public interface TokenVerifierPort {

  TokenClaims verifyAccessToken(String accessToken);
}
