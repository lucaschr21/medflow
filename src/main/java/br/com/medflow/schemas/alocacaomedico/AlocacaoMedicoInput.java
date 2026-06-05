package br.com.medflow.schemas.alocacaomedico;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Schema de entrada para alocações médicas.
 *
 * @param medicoId identificador do médico
 * @param consultorioId identificador do consultório
 * @param dataInicio data de início da alocação
 * @param dataFim data de fim da alocação
 */
public record AlocacaoMedicoInput(
    @NotNull(message = "O médico da alocação é obrigatório.")
    UUID medicoId,
    @NotNull(message = "O consultório da alocação é obrigatório.")
    UUID consultorioId,
    @NotNull(message = "A data de início da alocação é obrigatória.")
    LocalDate dataInicio,
    LocalDate dataFim) {
}
