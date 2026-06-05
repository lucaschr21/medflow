package br.com.medflow.core.security.identity;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Fornece acesso seguro ao usuário autenticado no contexto atual.
 *
 * <p>Exemplo de uso:
 *
 * <pre>{@code
 * AuthenticatedUser usuário = currentAuthenticatedUser.getRequired();
 * String username = usuário.username();
 * }</pre>
 */
@Component
public class CurrentAuthenticatedUser {

  /**
   * Retorna o usuário autenticado quando houver um principal compatível no
   * contexto de segurança.
   *
   * @return usuário autenticado opcional
   */
  public Optional<AuthenticatedUser> get() {
    return currentPrincipal().map(MedflowAuthenticatedPrincipal::user);
  }

  /**
   * Retorna o usuário autenticado ou falha quando não houver autenticação
   * aplicável.
   *
   * @return usuário autenticado atual
   * @throws IllegalStateException quando não houver usuário autenticado
   */
  public AuthenticatedUser getRequired() {
    return get().orElseThrow(() -> new IllegalStateException("No authenticated user is available"));
  }

  /**
   * Retorna o principal autenticado completo quando disponível.
   *
   * @return principal autenticado opcional
   */
  public Optional<MedflowAuthenticatedPrincipal> getPrincipal() {
    return currentPrincipal();
  }

  private static Optional<MedflowAuthenticatedPrincipal> currentPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof MedflowAuthenticatedPrincipal authenticatedPrincipal) {
      return Optional.of(authenticatedPrincipal);
    }

    return Optional.empty();
  }
}
