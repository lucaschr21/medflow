package br.com.medflow.services.rules.registroatendimento;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.enums.StatusConsulta;

/**
 * Regras de domínio aplicadas a registros de atendimento.
 */
@Component
public class RegistroAtendimentoRules {

  private static final Set<StatusConsulta> ALLOWED_STATUSES =
      EnumSet.of(StatusConsulta.EM_ATENDIMENTO, StatusConsulta.FINALIZADA);

  /**
   * Valida a coerência entre a consulta, o médico e o status atual.
   *
   * @param consulta consulta associada ao registro
   * @param medico médico associado ao registro
   */
  public void validateAssociations(Consulta consulta, Medico medico) {
    requireSame(consulta.getMedico().getId(), medico.getId(),
        "O médico do registro de atendimento deve ser o mesmo da consulta.");

    if (!ALLOWED_STATUSES.contains(consulta.getStatus())) {
      throw new BusinessRuleException(
          "O registro de atendimento só pode ser informado para consultas em atendimento ou finalizadas.");
    }
  }

  private void requireSame(UUID expected, UUID actual, String message) {
    if (!expected.equals(actual)) {
      throw new BusinessRuleException(message);
    }
  }
}
