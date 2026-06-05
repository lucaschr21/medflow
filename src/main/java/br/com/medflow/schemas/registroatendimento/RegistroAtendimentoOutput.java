package br.com.medflow.schemas.registroatendimento;

import java.util.UUID;

/**
 * Schema de saída para registros de atendimento.
 *
 * @param id identificador do registro
 * @param consultaId identificador da consulta
 * @param medicoId identificador do médico
 * @param queixaPrincipal queixa principal
 * @param anamnese anamnese
 * @param conduta conduta
 * @param observacoes observações complementares
 */
public record RegistroAtendimentoOutput(
    UUID id,
    UUID consultaId,
    UUID medicoId,
    String queixaPrincipal,
    String anamnese,
    String conduta,
    String observacoes) {
}
