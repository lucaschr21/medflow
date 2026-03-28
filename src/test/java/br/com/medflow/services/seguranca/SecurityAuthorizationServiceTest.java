package br.com.medflow.services.seguranca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.medflow.core.security.AuthorizationRequest;
import br.com.medflow.core.security.AuthorizationResult;
import br.com.medflow.core.security.SecurityErrorCode;
import br.com.medflow.core.security.TokenClaims;
import br.com.medflow.services.seguranca.ports.SecurityAuditPort;
import br.com.medflow.services.seguranca.ports.TokenRevocationPort;
import br.com.medflow.services.seguranca.ports.TokenVerifierPort;

class SecurityAuthorizationServiceTest {

  private static final Instant FIXED_NOW = Instant.parse("2026-03-28T15:00:00Z");

  private TokenVerifierPort tokenVerifierPort;
  private TokenRevocationPort tokenRevocationPort;
  private SpySecurityAuditPort securityAuditPort;
  private SecurityAuthorizationService service;

  @BeforeEach
  void setUp() {
    tokenVerifierPort = token -> validClaims();
    tokenRevocationPort = noRevokedTokens();
    securityAuditPort = new SpySecurityAuditPort();
    AuthorizationPolicy policy = new DefaultAuthorizationPolicy();

    service =
        new SecurityAuthorizationService(
            tokenVerifierPort,
            tokenRevocationPort,
            policy,
            securityAuditPort,
            Clock.fixed(FIXED_NOW, ZoneOffset.UTC));
  }

  @Test
  void shouldGrantWhenTokenHasAudienceAndRequiredScopes() {
    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.read"), Set.of("medflow-api"));

    AuthorizationResult result = service.authorize("valid-token", request);

    assertTrue(result.granted());
    assertEquals(1, securityAuditPort.grantedCalls.get());
    assertEquals(0, securityAuditPort.deniedCalls.get());
  }

  @Test
  void shouldDenyWhenScopeIsMissing() {
    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.write"), Set.of("medflow-api"));

    AuthorizationResult result = service.authorize("valid-token", request);

    assertFalse(result.granted());
    assertEquals(SecurityErrorCode.INSUFFICIENT_SCOPE, result.errorCode());
    assertEquals(0, securityAuditPort.grantedCalls.get());
    assertEquals(1, securityAuditPort.deniedCalls.get());
  }

  @Test
  void shouldDenyWhenAudienceDoesNotMatch() {
    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.read"), Set.of("finance-api"));

    AuthorizationResult result = service.authorize("valid-token", request);

    assertFalse(result.granted());
    assertEquals(SecurityErrorCode.INVALID_AUDIENCE, result.errorCode());
    assertEquals(1, securityAuditPort.deniedCalls.get());
  }

  @Test
  void shouldDenyWhenTokenIsExpired() {
    tokenVerifierPort =
        token ->
            new TokenClaims(
                "token-id",
                "https://id.medflow.local",
                "user-123",
                Set.of("medflow-api"),
                Set.of("consulta.read"),
                FIXED_NOW.minusSeconds(7200),
                FIXED_NOW.minusSeconds(7200),
                FIXED_NOW.minusSeconds(1));

    service =
        new SecurityAuthorizationService(
            tokenVerifierPort,
            tokenRevocationPort,
            new DefaultAuthorizationPolicy(),
            securityAuditPort,
            Clock.fixed(FIXED_NOW, ZoneOffset.UTC));

    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.read"), Set.of("medflow-api"));
    AuthorizationResult result = service.authorize("expired-token", request);

    assertFalse(result.granted());
    assertEquals(SecurityErrorCode.TOKEN_EXPIRED, result.errorCode());
    assertEquals(1, securityAuditPort.deniedCalls.get());
  }

  @Test
  void shouldDenyWhenTokenIsRevoked() {
    tokenRevocationPort = revokedToken("token-id");

    service =
        new SecurityAuthorizationService(
            tokenVerifierPort,
            tokenRevocationPort,
            new DefaultAuthorizationPolicy(),
            securityAuditPort,
            Clock.fixed(FIXED_NOW, ZoneOffset.UTC));

    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.read"), Set.of("medflow-api"));
    AuthorizationResult result = service.authorize("valid-token", request);

    assertFalse(result.granted());
    assertEquals(SecurityErrorCode.TOKEN_REVOKED, result.errorCode());
    assertEquals(1, securityAuditPort.deniedCalls.get());
  }

  @Test
  void shouldDenyWhenTokenIsInvalid() {
    tokenVerifierPort = token -> {
      throw new IllegalArgumentException("Token invalido");
    };

    service =
        new SecurityAuthorizationService(
            tokenVerifierPort,
            tokenRevocationPort,
            new DefaultAuthorizationPolicy(),
            securityAuditPort,
            Clock.fixed(FIXED_NOW, ZoneOffset.UTC));

    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.read"), Set.of("medflow-api"));
    AuthorizationResult result = service.authorize("invalid-token", request);

    assertFalse(result.granted());
    assertEquals(SecurityErrorCode.INVALID_TOKEN, result.errorCode());
    assertEquals(1, securityAuditPort.deniedCalls.get());
  }

  @Test
  void shouldDenyWhenAccessTokenIsBlank() {
    AuthorizationRequest request =
        new AuthorizationRequest(Set.of("consulta.read"), Set.of("medflow-api"));

    AuthorizationResult result = service.authorize("   ", request);

    assertFalse(result.granted());
    assertEquals(SecurityErrorCode.INVALID_TOKEN, result.errorCode());
    assertEquals(1, securityAuditPort.deniedCalls.get());
  }

  private static TokenClaims validClaims() {
    return new TokenClaims(
        "token-id",
        "https://id.medflow.local",
        "user-123",
        Set.of("medflow-api"),
        Set.of("consulta.read", "consulta.list"),
        FIXED_NOW.minusSeconds(600),
        FIXED_NOW.minusSeconds(600),
        FIXED_NOW.plusSeconds(3600));
  }

  private static class SpySecurityAuditPort implements SecurityAuditPort {

    private final AtomicInteger grantedCalls = new AtomicInteger();
    private final AtomicInteger deniedCalls = new AtomicInteger();

    @Override
    public void onAuthorizationGranted(TokenClaims tokenClaims, AuthorizationRequest request) {
      grantedCalls.incrementAndGet();
    }

    @Override
    public void onAuthorizationDenied(
        TokenClaims tokenClaims, AuthorizationRequest request, AuthorizationResult result) {
      deniedCalls.incrementAndGet();
    }
  }

  private static TokenRevocationPort noRevokedTokens() {
    return new TokenRevocationPort() {
      @Override
      public boolean isRevoked(String tokenId) {
        return false;
      }

      @Override
      public void revoke(String tokenId, Instant revokedAt) {
        // Nao utilizado neste teste.
      }
    };
  }

  private static TokenRevocationPort revokedToken(String revokedTokenId) {
    return new TokenRevocationPort() {
      @Override
      public boolean isRevoked(String tokenId) {
        return revokedTokenId.equals(tokenId);
      }

      @Override
      public void revoke(String tokenId, Instant revokedAt) {
        // Nao utilizado neste teste.
      }
    };
  }
}
