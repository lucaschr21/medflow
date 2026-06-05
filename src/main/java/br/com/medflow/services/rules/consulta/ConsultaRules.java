package br.com.medflow.services.rules.consulta;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.entities.AlocacaoMedico;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.Usuario;

/**
 * Regras de domínio aplicadas a consultas.
 */
@Component
public class ConsultaRules {

  /**
   * Valida a coerência entre usuário, médico, consultório e alocação.
   *
   * @param usuario usuário da consulta
   * @param medico médico da consulta
   * @param consultorio consultório da consulta
   * @param alocacaoMedico alocação médica da consulta
   */
  public void validateAssociations(
      Usuario usuario,
      Medico medico,
      Consultorio consultorio,
      AlocacaoMedico alocacaoMedico) {
    requireSame(alocacaoMedico.getMedico().getId(), medico.getId(),
        "A alocação médica informada não pertence ao médico da consulta.");
    requireSame(alocacaoMedico.getConsultorio().getId(), consultorio.getId(),
        "A alocação médica informada não pertence ao consultório da consulta.");
    requireSame(usuario.getOrganizacao().getId(), medico.getUsuario().getOrganizacao().getId(),
        "O usuário da consulta deve pertencer à mesma organização do médico.");
  }

  private void requireSame(UUID expected, UUID actual, String message) {
    if (!expected.equals(actual)) {
      throw new BusinessRuleException(message);
    }
  }
}
