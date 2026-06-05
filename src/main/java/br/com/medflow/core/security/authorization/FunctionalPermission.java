package br.com.medflow.core.security.authorization;

import java.util.Locale;

/**
 * Representa uma permissao funcional do Medflow no formato
 * {@code recurso:acao}.
 *
 * @param resource nome logico do recurso
 * @param action acao funcional autorizada
 */
public record FunctionalPermission(String resource, ResourceAction action) {

  /**
   * Cria a permissao funcional com recurso normalizado.
   *
   * @param resource nome logico do recurso
   * @param action acao funcional autorizada
   */
  public FunctionalPermission {
    resource = normalizeResource(resource);
  }

  /**
   * Retorna a authority usada pelo Spring Security para esta permissao.
   *
   * @return authority no formato {@code recurso:acao}
   */
  public String authority() {
    return resource + ":" + action.value();
  }

  /**
   * Cria uma permissao funcional a partir de um recurso e uma acao.
   *
   * @param resource nome logico do recurso
   * @param action acao funcional
   * @return permissao funcional criada
   */
  public static FunctionalPermission of(String resource, ResourceAction action) {
    return new FunctionalPermission(resource, action);
  }

  private static String normalizeResource(String resource) {
    if (resource == null) {
      throw new IllegalArgumentException("resource cannot be null");
    }

    String normalized = resource.strip().toLowerCase(Locale.ROOT);
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("resource cannot be blank");
    }

    return normalized;
  }
}
