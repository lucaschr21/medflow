package br.com.medflow.core.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;

/**
 * Resolve o auditor atual para o JPA Auditing.
 *
 * <p>Quando não houver usuário autenticado no contexto atual, o auditor
 * padrão será {@value #SYSTEM_AUDITOR}.
 */
public class CurrentAuditorAware implements AuditorAware<String> {

  private static final String SYSTEM_AUDITOR = "SISTEMA";

  private final CurrentAuthenticatedUser currentAuthenticatedUser;

  /**
   * Cria o resolvedor do auditor atual.
   *
   * @param currentAuthenticatedUser acesso ao usuário autenticado atual
   */
  public CurrentAuditorAware(CurrentAuthenticatedUser currentAuthenticatedUser) {
    this.currentAuthenticatedUser = currentAuthenticatedUser;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<String> getCurrentAuditor() {
    return currentAuthenticatedUser.get()
        .map(AuthenticatedUser::username)
        .or(() -> Optional.of(SYSTEM_AUDITOR));
  }
}
