package br.com.medflow.core.security.identity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

/**
 * Principal autenticado da aplicacao.
 *
 * <p>Este tipo encapsula a identidade tipada do Medflow e preserva os
 * atributos e authorities originais produzidos pela autenticacao OAuth2.
 */
public record MedflowAuthenticatedPrincipal(
    AuthenticatedUser user,
    Map<String, Object> attributes,
    Collection<? extends GrantedAuthority> authorities)
    implements OAuth2AuthenticatedPrincipal {

  /**
   * Cria um principal autenticado imutavel.
   *
   * @param user identidade tipada da aplicacao
   * @param attributes atributos crus do principal OAuth2
   * @param authorities authorities associadas ao principal
   */
  public MedflowAuthenticatedPrincipal {
    attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    authorities = authorities == null ? java.util.List.of() : java.util.List.copyOf(authorities);
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * Retorna uma claim crua do token por nome.
   *
   * @param claimName nome da claim
   * @return valor opcional da claim
   */
  public Optional<Object> claim(String claimName) {
    if (claimName == null || claimName.isBlank()) {
      return Optional.empty();
    }

    return Optional.ofNullable(attributes.get(claimName));
  }

  /**
   * Retorna uma claim textual do token por nome.
   *
   * @param claimName nome da claim
   * @return valor textual opcional da claim
   */
  public Optional<String> stringClaim(String claimName) {
    return claim(claimName).map(String::valueOf).map(String::strip).filter(value -> !value.isBlank());
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    if (user.username() != null) {
      return user.username();
    }

    return user.subject();
  }
}
