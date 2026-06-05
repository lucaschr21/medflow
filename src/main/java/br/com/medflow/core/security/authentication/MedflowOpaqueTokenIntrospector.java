package br.com.medflow.core.security.authentication;

import java.util.Objects;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import br.com.medflow.core.security.authorization.TokenAuthoritiesMapper;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

/**
 * Introspector que delega a validacao do token ao authorization server e
 * converte o principal resultante para o formato tipado do Medflow.
 */
public class MedflowOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

  private final OpaqueTokenIntrospector delegate;
  private final AuthenticatedUserMapper authenticatedUserMapper;
  private final TokenAuthoritiesMapper tokenAuthoritiesMapper;

  /**
   * Cria o introspector do Medflow.
   *
   * @param delegate                    introspector que valida o token no
   *                                    authorization server
   * @param authenticatedUserMapper     mapper do principal autenticado
   * @param tokenAuthoritiesMapper mapper das authorities do principal
   */
  public MedflowOpaqueTokenIntrospector(
      OpaqueTokenIntrospector delegate,
      AuthenticatedUserMapper authenticatedUserMapper,
      TokenAuthoritiesMapper tokenAuthoritiesMapper) {
    this.delegate = Objects.requireNonNull(delegate);
    this.authenticatedUserMapper = Objects.requireNonNull(authenticatedUserMapper);
    this.tokenAuthoritiesMapper = Objects.requireNonNull(tokenAuthoritiesMapper);
  }

  /** {@inheritDoc} */
  @Override
  public OAuth2AuthenticatedPrincipal introspect(String token) {
    OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);
    MedflowAuthenticatedPrincipal mappedPrincipal = authenticatedUserMapper.map(principal);
    return new MedflowAuthenticatedPrincipal(
        mappedPrincipal.user(),
        mappedPrincipal.getAttributes(),
        tokenAuthoritiesMapper.convert(mappedPrincipal));
  }
}
