package br.com.medflow.schemas.registroatendimento;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para registros de atendimento.
 *
 * @param consultaId identificador da consulta
 * @param medicoId identificador do médico
 * @param queixaPrincipal queixa principal
 * @param anamnese anamnese
 * @param conduta conduta
 * @param observacoes observações complementares
 */
public record RegistroAtendimentoInput(
    @NotNull(message = "A consulta do registro de atendimento é obrigatória.")
    UUID consultaId,
    @NotNull(message = "O médico do registro de atendimento é obrigatório.")
    UUID medicoId,
    @NotBlank(message = "A queixa principal é obrigatória.")
    @Size(max = 500, message = "A queixa principal deve ter no máximo 500 caracteres.")
    String queixaPrincipal,
    @NotBlank(message = "A anamnese é obrigatória.")
    String anamnese,
    @NotBlank(message = "A conduta é obrigatória.")
    String conduta,
    @Size(max = 10000, message = "As observações devem ter no máximo 10000 caracteres.")
    String observacoes) {
}
