package br.com.medflow.core.exceptions;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 */
public class BusinessRuleException extends RuntimeException {

  private final ErrorCode code;

  /**
   * Cria a exceção com o código de erro e a mensagem informada.
   *
   * @param code    código padronizado do erro
   * @param message mensagem da violação
   */
  public BusinessRuleException(ErrorCode code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Cria a exceção com apenas a mensagem (código padrão
   * {@link ErrorCode#VALIDATION_ERROR}).
   *
   * @param message mensagem da violação
   */
  public BusinessRuleException(String message) {
    this(ErrorCode.VALIDATION_ERROR, message);
  }

  /**
   * Retorna o código padronizado do erro.
   *
   * @return código do erro
   */
  public ErrorCode getCode() {
    return code;
  }
}
