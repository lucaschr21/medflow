package br.com.medflow.schemas.especialidade;

import java.util.UUID;

/**
 * Schema de saída para especialidades.
 *
 * @param id identificador da especialidade
 * @param nome nome da especialidade
 * @param descricao descrição da especialidade
 */
public record EspecialidadeOutput(
    UUID id,
    String nome,
    String descricao) {
}
