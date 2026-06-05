package br.com.medflow.entities.enums;

/**
 * Estados de ciclo de vida de uma consulta.
 */
public enum StatusConsulta {
  AGENDADA,
  CONFIRMADA,
  EM_ESPERA,
  EM_ATENDIMENTO,
  FINALIZADA,
  CANCELADA,
  NAO_COMPARECEU
}
