package br.com.medflow.core.security.authorization;

import java.util.Locale;

/**
 * Enumera as acoes funcionais padrao do Medflow.
 */
public enum ResourceAction {
  CREATE("create"),
  READ("read"),
  UPDATE("update"),
  DEACTIVATE("deactivate"),
  DELETE("delete");

  private final String value;

  ResourceAction(String value) {
    this.value = value;
  }

  /**
   * Retorna o valor textual da acao funcional.
   *
   * @return valor textual da acao
   */
  public String value() {
    return value;
  }

  /**
   * Converte uma representacao textual em {@link ResourceAction}.
   *
   * @param value valor textual da acao
   * @return acao correspondente
   */
  public static ResourceAction from(String value) {
    String normalized = normalize(value);
    return switch (normalized) {
      case "create" -> CREATE;
      case "read" -> READ;
      case "update" -> UPDATE;
      case "deactivate" -> DEACTIVATE;
      case "delete" -> DELETE;
      default -> throw new IllegalArgumentException("Unsupported resource action: " + value);
    };
  }

  private static String normalize(String value) {
    if (value == null) {
      throw new IllegalArgumentException("value cannot be null");
    }

    String normalized = value.strip().toLowerCase(Locale.ROOT);
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("value cannot be blank");
    }

    return normalized;
  }
}
