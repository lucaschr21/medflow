package br.com.medflow.schemas.consultorio;

import java.util.UUID;

/**
 * Schema de saída para consultórios.
 *
 * @param id identificador do consultório
 * @param unidadeId identificador da unidade
 * @param nome nome do consultório
 * @param sala sala do consultório
 * @param ativo indicador de atividade
 */
public record ConsultorioOutput(
    UUID id,
    UUID unidadeId,
    String nome,
    String sala,
    boolean ativo) {
}
