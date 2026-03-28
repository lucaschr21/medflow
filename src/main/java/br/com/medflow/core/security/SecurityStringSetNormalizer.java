package br.com.medflow.core.security;

import java.util.Set;
import java.util.stream.Collectors;

final class SecurityStringSetNormalizer {

  private SecurityStringSetNormalizer() {}

  static Set<String> normalize(Set<String> values) {
    if (values == null || values.isEmpty()) {
      return Set.of();
    }

    return values.stream()
        .map(value -> value == null ? "" : value.strip())
        .filter(value -> !value.isBlank())
        .collect(Collectors.toUnmodifiableSet());
  }
}
