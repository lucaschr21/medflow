package br.com.medflow.core.security.authorization;

import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import br.com.medflow.core.security.annotations.ProtectedResource;

/**
 * Resolve o nome funcional de um recurso a partir de um tipo da aplicacao.
 *
 * <p>
 * O resolvedor usa {@link ProtectedResource} quando presente. Caso contrario,
 * deriva o nome do recurso a partir do nome simples da classe em
 * {@code kebab-case}.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProtectedResourceResolver {

  private static final Pattern CAMEL_CASE_BOUNDARY = Pattern.compile("([a-z0-9])([A-Z])");
  private static final ClassValue<String> RESOURCE_NAMES = new ClassValue<>() {
    @Override
    protected String computeValue(Class<?> type) {
      ProtectedResource protectedResource = AnnotatedElementUtils.findMergedAnnotation(type, ProtectedResource.class);
      return protectedResource != null
          ? normalize(protectedResource.value())
          : camelToKebab(type.getSimpleName());
    }
  };

  /**
   * Resolve o nome funcional do recurso para um tipo.
   *
   * @param resourceType tipo do recurso
   * @return nome funcional resolvido
   */
  public String resolve(Class<?> resourceType) {
    if (resourceType == null) {
      throw new IllegalArgumentException("resourceType cannot be null");
    }

    return RESOURCE_NAMES.get(resourceType);
  }

  private static String camelToKebab(String value) {
    return normalize(CAMEL_CASE_BOUNDARY.matcher(value).replaceAll("$1-$2"));
  }

  private static String normalize(String value) {
    if (value == null) {
      throw new IllegalArgumentException("resource name cannot be null");
    }

    String normalized = value.strip().toLowerCase(Locale.ROOT);
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("resource name cannot be blank");
    }

    return normalized;
  }
}
