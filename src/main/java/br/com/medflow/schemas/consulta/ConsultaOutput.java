package br.com.medflow.schemas.consulta;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.medflow.entities.enums.StatusConsulta;

/**
 * Schema de saída para consultas.
 *
 * @param id identificador da consulta
 * @param usuarioId identificador do usuário
 * @param medicoId identificador do médico
 * @param consultorioId identificador do consultório
 * @param alocacaoMedicoId identificador da alocação médica
 * @param dataHoraInicio data e hora de início
 * @param dataHoraFim data e hora de fim
 * @param status status da consulta
 * @param tipoConsulta tipo da consulta
 * @param motivo motivo da consulta
 * @param registroAtendimentoId identificador do registro de atendimento, quando existir
 */
public record ConsultaOutput(
    UUID id,
    UUID usuarioId,
    UUID medicoId,
    UUID consultorioId,
    UUID alocacaoMedicoId,
    LocalDateTime dataHoraInicio,
    LocalDateTime dataHoraFim,
    StatusConsulta status,
    String tipoConsulta,
    String motivo,
    UUID registroAtendimentoId) {
}
