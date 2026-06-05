package br.com.medflow.services.rules.alocacaomedico;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;

/**
 * Regras de domínio aplicadas a alocações médicas.
 */
@Component
public class AlocacaoMedicoRules {

  /**
   * Valida a coerência entre o médico e o consultório da alocação.
   *
   * @param medico médico da alocação
   * @param consultorio consultório da alocação
   */
  public void validateAssociations(Medico medico, Consultorio consultorio) {
    requireSame(
        medico.getUsuario().getOrganizacao().getId(),
        consultorio.getUnidade().getOrganizacao().getId(),
        "O médico e o consultório da alocação devem pertencer à mesma organização.");
  }

  private void requireSame(UUID expected, UUID actual, String message) {
    if (!expected.equals(actual)) {
      throw new BusinessRuleException(message);
    }
  }
}
