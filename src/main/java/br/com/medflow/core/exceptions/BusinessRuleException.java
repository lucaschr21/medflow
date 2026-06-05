package br.com.medflow.core.exceptions;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 */
public class BusinessRuleException extends RuntimeException {

  /**
   * Cria a exceção com a mensagem informada.
   *
   * @param message mensagem da violação
   */
  public BusinessRuleException(String message) {
    super(message);
  }
}
