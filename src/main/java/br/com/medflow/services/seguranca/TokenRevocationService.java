package br.com.medflow.services.seguranca;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.services.seguranca.ports.TokenRevocationPort;
import lombok.RequiredArgsConstructor;

@Service
@ConditionalOnProperty(name = "medflow.security.oauth2.enabled", havingValue = "true")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenRevocationService {

  private final TokenRevocationPort tokenRevocationPort;
  private final Clock securityClock;

  @Transactional
  public void revogar(String tokenId) {
    String normalizedTokenId = normalizeAndValidateTokenId(tokenId);

    Instant revokedAt = Instant.now(securityClock);
    tokenRevocationPort.revoke(normalizedTokenId, revokedAt);
  }

  private static String normalizeAndValidateTokenId(String tokenId) {
    String normalizedTokenId = Objects.requireNonNull(tokenId, "JTI do token é obrigatório").trim();
    if (normalizedTokenId.isBlank()) {
      throw new BusinessRuleException("JTI do token é obrigatório");
    }
    if (normalizedTokenId.length() > 255) {
      throw new BusinessRuleException("JTI do token deve ter no máximo 255 caracteres");
    }
    return normalizedTokenId;
  }
}
