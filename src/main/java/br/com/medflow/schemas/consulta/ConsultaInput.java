package br.com.medflow.schemas.consulta;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.medflow.entities.enums.StatusConsulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para consultas.
 *
 * @param usuarioId identificador do usuário
 * @param medicoId identificador do médico
 * @param consultorioId identificador do consultório
 * @param alocacaoMedicoId identificador da alocação médica
 * @param dataHoraInicio data e hora de início
 * @param dataHoraFim data e hora de fim
 * @param status status da consulta
 * @param tipoConsulta tipo da consulta
 * @param motivo motivo da consulta
 */
public record ConsultaInput(
    @NotNull(message = "O usuário da consulta é obrigatório.")
    UUID usuarioId,
    @NotNull(message = "O médico da consulta é obrigatório.")
    UUID medicoId,
    @NotNull(message = "O consultório da consulta é obrigatório.")
    UUID consultorioId,
    @NotNull(message = "A alocação médica da consulta é obrigatória.")
    UUID alocacaoMedicoId,
    @NotNull(message = "A data e hora de início da consulta é obrigatória.")
    LocalDateTime dataHoraInicio,
    @NotNull(message = "A data e hora de fim da consulta é obrigatória.")
    LocalDateTime dataHoraFim,
    @NotNull(message = "O status da consulta é obrigatório.")
    StatusConsulta status,
    @NotBlank(message = "O tipo da consulta é obrigatório.")
    @Size(max = 80, message = "O tipo da consulta deve ter no máximo 80 caracteres.")
    String tipoConsulta,
    @NotBlank(message = "O motivo da consulta é obrigatório.")
    @Size(max = 500, message = "O motivo da consulta deve ter no máximo 500 caracteres.")
    String motivo) {
}
