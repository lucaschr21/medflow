package br.com.medflow.core.exceptions;

/**
 * Sinaliza que uma entidade obrigatória não foi localizada.
 */
public class EntityNotFoundException extends RuntimeException {

  /**
   * Cria uma exceção com a mensagem informada.
   *
   * @param message descrição do erro
   */
  public EntityNotFoundException(String message) {
    super(message);
  }
}
