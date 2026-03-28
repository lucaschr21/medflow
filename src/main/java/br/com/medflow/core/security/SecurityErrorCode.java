package br.com.medflow.core.security;

public enum SecurityErrorCode {
  INVALID_TOKEN,
  TOKEN_EXPIRED,
  TOKEN_NOT_YET_VALID,
  TOKEN_REVOKED,
  INVALID_AUDIENCE,
  INSUFFICIENT_SCOPE
}
