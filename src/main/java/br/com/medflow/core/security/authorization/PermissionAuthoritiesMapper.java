package br.com.medflow.core.security.authorization;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

/**
 * Converte claims do token em {@link GrantedAuthority} do Medflow.
 *
 * <p>
 * As authorities produzidas por este mapper servem como base para a
 * autorizacao funcional via {@code hasAuthority('recurso:acao')}.
 */
@Component
public class PermissionAuthoritiesMapper
    implements Converter<OAuth2AuthenticatedPrincipal, Collection<? extends GrantedAuthority>> {

  /**
   * Converte o principal autenticado em authorities funcionais e de papel.
   *
   * @param principal principal autenticado
   * @return authorities produzidas para o principal
   */
  @Override
  public Collection<? extends GrantedAuthority> convert(OAuth2AuthenticatedPrincipal principal) {
    LinkedHashSet<String> authorities = new LinkedHashSet<>();
    principal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .forEach(authorities::add);

    if (principal instanceof MedflowAuthenticatedPrincipal medflowPrincipal) {
      addClientRoleAuthorities(authorities, medflowPrincipal.user());
    }

    addScopedAuthorities(authorities, principal.getAttributes().get("scope"));
    addPermissionAuthorities(authorities, principal.getAttributes().get("authorization"));
    addPermissionAuthorities(authorities, principal.getAttributes().get("permissions"));

    return authorities.stream().map(SimpleGrantedAuthority::new).toList();
  }

  private static void addClientRoleAuthorities(
      Set<String> authorities, AuthenticatedUser authenticatedUser) {
    authenticatedUser.resourceRoles().values().stream()
        .flatMap(Collection::stream)
        .map(PermissionAuthoritiesMapper::roleAuthority)
        .forEach(authorities::add);
  }

  private static void addScopedAuthorities(Set<String> authorities, Object scopeClaim) {
    readScopes(scopeClaim).stream()
        .filter(scope -> scope.contains(":"))
        .forEach(authorities::add);
  }

  private static void addPermissionAuthorities(Set<String> authorities, Object permissionsClaim) {
    if (permissionsClaim instanceof Map<?, ?> authorizationClaim) {
      addPermissionAuthorities(authorities, authorizationClaim.get("permissions"));
      return;
    }

    if (!(permissionsClaim instanceof Collection<?> permissions)) {
      return;
    }

    permissions.stream()
        .map(PermissionAuthoritiesMapper::asPermissionEntries)
        .forEach(entries -> authorities.addAll(entries));
  }

  private static Set<String> asPermissionEntries(Object permissionObject) {
    if (!(permissionObject instanceof Map<?, ?> permissionMap)) {
      return Set.of();
    }

    String resourceName = resourceName(permissionMap);
    if (resourceName == null) {
      return Set.of();
    }

    Object scopesObject = permissionMap.get("scopes");
    if (!(scopesObject instanceof Collection<?> scopes)) {
      return Set.of();
    }

    return scopes.stream()
        .map(PermissionAuthoritiesMapper::normalize)
        .filter(java.util.Objects::nonNull)
        .map(scope -> FunctionalPermission.of(resourceName, ResourceAction.from(scope)).authority())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private static Set<String> readScopes(Object scopeClaim) {
    if (scopeClaim instanceof String scopeValue) {
      return java.util.Arrays.stream(scopeValue.split("\\s+"))
          .map(PermissionAuthoritiesMapper::normalize)
          .filter(java.util.Objects::nonNull)
          .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    if (scopeClaim instanceof Collection<?> scopes) {
      return scopes.stream()
          .map(PermissionAuthoritiesMapper::normalize)
          .filter(java.util.Objects::nonNull)
          .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    return Set.of();
  }

  private static String resourceName(Map<?, ?> permissionMap) {
    String rsname = normalize(permissionMap.get("rsname"));
    return rsname != null ? rsname : normalize(permissionMap.get("resource_name"));
  }

  private static String roleAuthority(String role) {
    return "ROLE_" + role;
  }

  private static String normalize(Object value) {
    if (value == null) {
      return null;
    }

    String normalized = String.valueOf(value).strip();
    return normalized.isBlank() ? null : normalized;
  }
}
