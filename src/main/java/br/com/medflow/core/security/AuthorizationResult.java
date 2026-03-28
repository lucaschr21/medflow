package br.com.medflow.core.security;

public record AuthorizationResult(boolean granted, SecurityErrorCode errorCode, String reason) {

  public AuthorizationResult {
    if (granted) {
      if (errorCode != null) {
        throw new IllegalArgumentException("Resultado autorizado não pode conter código de erro");
      }
      if (reason == null || reason.isBlank()) {
        throw new IllegalArgumentException("Resultado autorizado deve conter motivo");
      }
    } else {
      if (errorCode == null) {
        throw new IllegalArgumentException("Resultado negado deve conter código de erro");
      }

      if (reason == null || reason.isBlank()) {
        throw new IllegalArgumentException("Resultado negado deve conter motivo");
      }
    }
  }

  public static AuthorizationResult allow() {
    return new AuthorizationResult(true, null, "Autorizado");
  }

  public static AuthorizationResult deny(SecurityErrorCode errorCode, String reason) {
    return new AuthorizationResult(false, errorCode, reason);
  }
}
