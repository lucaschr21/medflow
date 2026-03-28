package br.com.medflow.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.medflow.services.seguranca.TokenRevocationService;

class OAuth2TokenRevocationControllerTest {

  @Test
  void shouldDelegateTokenIdToRevocationService() {
    TokenRevocationService tokenRevocationService = Mockito.mock(TokenRevocationService.class);
    OAuth2TokenRevocationController controller =
        new OAuth2TokenRevocationController(tokenRevocationService);

    controller.revoke("token-id-123");

    verify(tokenRevocationService, times(1)).revogar("token-id-123");
  }
}
