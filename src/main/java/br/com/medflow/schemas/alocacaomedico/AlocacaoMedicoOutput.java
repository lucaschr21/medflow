package br.com.medflow.schemas.alocacaomedico;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Schema de saída para alocações médicas.
 *
 * @param id identificador da alocação
 * @param medicoId identificador do médico
 * @param consultorioId identificador do consultório
 * @param dataInicio data de início da alocação
 * @param dataFim data de fim da alocação
 * @param ativo indicador de atividade
 */
public record AlocacaoMedicoOutput(
    UUID id,
    UUID medicoId,
    UUID consultorioId,
    LocalDate dataInicio,
    LocalDate dataFim,
    boolean ativo) {
}
