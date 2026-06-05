package br.com.medflow.core.security.authorization;

import org.hibernate.annotations.SoftDelete;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * Resolve a ação funcional de um recurso a partir do método HTTP atual.
 *
 * <p>Para {@code DELETE}, a ação depende da capacidade do recurso:
 * recursos com {@link SoftDelete} são tratados como {@link ResourceAction#DEACTIVATE};
 * os demais permanecem como {@link ResourceAction#DELETE}.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ResourceActionResolver {

  private static final ClassValue<Boolean> SOFT_DELETE_TYPES = new ClassValue<>() {
    @Override
    protected Boolean computeValue(Class<?> type) {
      return AnnotatedElementUtils.hasAnnotation(type, SoftDelete.class);
    }
  };

  /**
   * Resolve a ação funcional do recurso para o método HTTP informado.
   *
   * @param resourceType tipo do recurso protegido
   * @param httpMethod método HTTP atual
   * @return ação funcional correspondente
   */
  public ResourceAction resolve(Class<?> resourceType, HttpMethod httpMethod) {
    if (resourceType == null) {
      throw new IllegalArgumentException("resourceType cannot be null");
    }
    if (httpMethod == null) {
      throw new IllegalArgumentException("httpMethod cannot be null");
    }

    return switch (httpMethod.name()) {
      case "GET", "HEAD", "OPTIONS" -> ResourceAction.READ;
      case "POST" -> ResourceAction.CREATE;
      case "PUT", "PATCH" -> ResourceAction.UPDATE;
      case "DELETE" -> SOFT_DELETE_TYPES.get(resourceType)
          ? ResourceAction.DEACTIVATE
          : ResourceAction.DELETE;
      default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
    };
  }
}
