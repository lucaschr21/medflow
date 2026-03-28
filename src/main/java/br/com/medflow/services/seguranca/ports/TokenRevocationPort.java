package br.com.medflow.services.seguranca.ports;

import java.time.Instant;

public interface TokenRevocationPort {

  boolean isRevoked(String tokenId);

  void revoke(String tokenId, Instant revokedAt);
}
