package br.com.medflow.core.security.authorization;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;
import br.com.medflow.core.security.config.AuthorizationProperties;

/**
 * Converte o principal autenticado em authorities locais do backend.
 *
 * <p>Este mapper não decide permissões funcionais do domínio. A autorização
 * funcional é consultada sob demanda no Keycloak Authorization Services.
 */
@Component
public class TokenAuthoritiesMapper
    implements Converter<OAuth2AuthenticatedPrincipal, Collection<? extends GrantedAuthority>> {

  private final String clientId;

  /**
   * Cria o mapper de authorities locais do backend.
   *
   * @param authorizationProperties propriedades da autorização funcional
   */
  public TokenAuthoritiesMapper(AuthorizationProperties authorizationProperties) {
    this.clientId = Objects.requireNonNull(authorizationProperties).audience();
  }

  /**
   * Converte o principal autenticado em authorities locais.
   *
   * @param principal principal autenticado
   * @return authorities preservadas e papéis de client convertidos para
   *         {@code ROLE_*}
   */
  @Override
  public Collection<? extends GrantedAuthority> convert(OAuth2AuthenticatedPrincipal principal) {
    LinkedHashSet<String> authorities = new LinkedHashSet<>();
    principal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .forEach(authorities::add);

    if (principal instanceof MedflowAuthenticatedPrincipal medflowPrincipal) {
      addClientRoleAuthorities(authorities, medflowPrincipal.user(), clientId);
    }

    return authorities.stream().map(SimpleGrantedAuthority::new).toList();
  }

  private static void addClientRoleAuthorities(
      Set<String> authorities, AuthenticatedUser authenticatedUser, String clientId) {
    authenticatedUser.clientRoles(clientId).stream()
        .map(TokenAuthoritiesMapper::roleAuthority)
        .forEach(authorities::add);
  }

  private static String roleAuthority(String role) {
    return "ROLE_" + role;
  }
}
