package br.com.medflow.controllers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.medflow.services.seguranca.TokenRevocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@ConditionalOnProperty(name = "medflow.security.oauth2.enabled", havingValue = "true")
@Validated
@RequiredArgsConstructor
@RequestMapping("/oauth2")
@Tag(name = "OAuth2")
public class OAuth2TokenRevocationController {

  private final TokenRevocationService tokenRevocationService;

  @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void revoke(
      @RequestParam("token")
          @NotBlank(message = "Token é obrigatório")
          @Size(max = 255, message = "Token excede tamanho máximo permitido")
          String tokenId) {
    tokenRevocationService.revogar(tokenId);
  }
}
