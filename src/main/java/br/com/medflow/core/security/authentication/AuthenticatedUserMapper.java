package br.com.medflow.core.security.authentication;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

/**
 * Converte o principal autenticado pelo resource server em um principal tipado
 * da aplicacao.
 */
@Component
public class AuthenticatedUserMapper {

  /**
   * Converte um principal OAuth2 em {@link MedflowAuthenticatedPrincipal}.
   *
   * @param principal principal original retornado pelo introspector
   * @return principal tipado da aplicacao
   */
  public MedflowAuthenticatedPrincipal map(OAuth2AuthenticatedPrincipal principal) {
    Map<String, Object> attributes = Map.copyOf(principal.getAttributes());
    AuthenticatedUser user = new AuthenticatedUser(
        requiredStringAttribute(attributes, "sub"),
        firstNonBlank(
            stringAttribute(attributes, "preferred_username"),
            stringAttribute(attributes, "username"),
            principal.getName()),
        stringAttribute(attributes, "email"),
        stringAttribute(attributes, "name"),
        requiredStringAttribute(attributes, "cpf"),
        requiredStringAttribute(attributes, "telefone"),
        requiredLocalDateAttribute(attributes, "dataNascimento"),
        readRoles(attributes.get("realm_access")),
        readResourceRoles(attributes.get("resource_access")),
        readGroups(attributes.get("groups")));

    return new MedflowAuthenticatedPrincipal(user, attributes, principal.getAuthorities());
  }

  private static Set<String> readRoles(Object claimValue) {
    if (!(claimValue instanceof Map<?, ?> claimMap)) {
      return Set.of();
    }

    Object rolesValue = claimMap.get("roles");
    if (!(rolesValue instanceof Collection<?> roles)) {
      return Set.of();
    }

    return roles.stream()
        .map(AuthenticatedUserMapper::normalize)
        .filter(Objects::nonNull)
        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
  }

  private static Map<String, Set<String>> readResourceRoles(Object claimValue) {
    if (!(claimValue instanceof Map<?, ?> resources)) {
      return Map.of();
    }

    Map<String, Set<String>> mappedRoles = new LinkedHashMap<>();
    resources.forEach(
        (clientId, clientValue) -> {
          String normalizedClientId = normalize(clientId);
          if (normalizedClientId == null) {
            return;
          }

          Set<String> roles = readRoles(clientValue);
          if (!roles.isEmpty()) {
            mappedRoles.put(normalizedClientId, roles);
          }
        });
    return Map.copyOf(mappedRoles);
  }

  private static Set<String> readGroups(Object claimValue) {
    if (!(claimValue instanceof Collection<?> groups)) {
      return Set.of();
    }

    return groups.stream()
        .map(AuthenticatedUserMapper::normalize)
        .filter(Objects::nonNull)
        .map(AuthenticatedUserMapper::normalizeGroup)
        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
  }

  private static String stringAttribute(Map<String, Object> attributes, String attributeName) {
    return normalize(attributes.get(attributeName));
  }

  private static String requiredStringAttribute(
      Map<String, Object> attributes, String attributeName) {
    String value = stringAttribute(attributes, attributeName);
    if (value == null) {
      throw new IllegalStateException("Required token claim is missing: " + attributeName);
    }
    return value;
  }

  private static LocalDate requiredLocalDateAttribute(
      Map<String, Object> attributes, String attributeName) {
    return LocalDate.parse(requiredStringAttribute(attributes, attributeName));
  }

  private static String firstNonBlank(String... values) {
    for (String value : values) {
      String normalized = normalize(value);
      if (normalized != null) {
        return normalized;
      }
    }
    return null;
  }

  private static String normalizeGroup(String group) {
    return group.startsWith("/") ? group.substring(1) : group;
  }

  private static String normalize(Object value) {
    if (value == null) {
      return null;
    }

    String normalized = String.valueOf(value).strip();
    return normalized.isBlank() ? null : normalized;
  }
}
