package br.com.medflow.schemas.agendamedica;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Schema de entrada para agendas médicas.
 *
 * @param alocacaoMedicoId identificador da alocação médica
 * @param diaSemana dia da semana da agenda
 * @param horaInicio hora de início
 * @param horaFim hora de fim
 */
public record AgendaMedicaInput(
    @NotNull(message = "A alocação médica da agenda é obrigatória.")
    UUID alocacaoMedicoId,
    @NotNull(message = "O dia da semana da agenda é obrigatório.")
    DayOfWeek diaSemana,
    @NotNull(message = "A hora de início da agenda é obrigatória.")
    LocalTime horaInicio,
    @NotNull(message = "A hora de fim da agenda é obrigatória.")
    LocalTime horaFim) {
}
