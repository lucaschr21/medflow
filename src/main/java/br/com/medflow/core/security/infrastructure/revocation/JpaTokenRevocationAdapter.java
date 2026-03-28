package br.com.medflow.core.security.infrastructure.revocation;

import java.time.Instant;
import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.entities.seguranca.TokenRevogado;
import br.com.medflow.repositories.seguranca.TokenRevogadoRepository;
import br.com.medflow.services.seguranca.ports.TokenRevocationPort;

public class JpaTokenRevocationAdapter implements TokenRevocationPort {

  private final TokenRevogadoRepository tokenRevogadoRepository;

  public JpaTokenRevocationAdapter(TokenRevogadoRepository tokenRevogadoRepository) {
    this.tokenRevogadoRepository =
      Objects.requireNonNull(tokenRevogadoRepository, "TokenRevogadoRepository é obrigatório");
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isRevoked(String tokenId) {
    String normalizedTokenId = normalizeTokenId(tokenId);
    if (normalizedTokenId == null) {
      return false;
    }
    return tokenRevogadoRepository.existsByJti(normalizedTokenId);
  }

  @Override
  @Transactional
  public void revoke(String tokenId, Instant revokedAt) {
    String normalizedTokenId = normalizeTokenId(tokenId);
    if (normalizedTokenId == null) {
      throw new IllegalArgumentException("JTI do token é obrigatório");
    }

    Instant revocationInstant = revokedAt == null ? Instant.now() : revokedAt;
    try {
      tokenRevogadoRepository.save(new TokenRevogado(normalizedTokenId, revocationInstant));
    } catch (DataIntegrityViolationException ex) {
      // JTI já revogado por outra transação concorrente.
    }
  }

  private static String normalizeTokenId(String tokenId) {
    if (tokenId == null) {
      return null;
    }
    String normalizedTokenId = tokenId.trim();
    return normalizedTokenId.isEmpty() ? null : normalizedTokenId;
  }
}
