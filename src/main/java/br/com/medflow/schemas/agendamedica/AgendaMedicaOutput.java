package br.com.medflow.schemas.agendamedica;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Schema de saída para agendas médicas.
 *
 * @param id identificador da agenda
 * @param alocacaoMedicoId identificador da alocação médica
 * @param diaSemana dia da semana da agenda
 * @param horaInicio hora de início
 * @param horaFim hora de fim
 * @param ativo indicador de atividade
 */
public record AgendaMedicaOutput(
    UUID id,
    UUID alocacaoMedicoId,
    DayOfWeek diaSemana,
    LocalTime horaInicio,
    LocalTime horaFim,
    boolean ativo) {
}
