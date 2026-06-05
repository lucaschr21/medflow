package br.com.medflow.schemas.especialidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para especialidades.
 *
 * @param nome nome da especialidade
 * @param descricao descrição da especialidade
 * @param ativo indicador de atividade
 */
public record EspecialidadeInput(
    @NotBlank(message = "O nome da especialidade é obrigatório.")
    @Size(max = 120, message = "O nome da especialidade deve ter no máximo 120 caracteres.")
    String nome,
    @Size(max = 500, message = "A descrição da especialidade deve ter no máximo 500 caracteres.")
    String descricao,
    boolean ativo) {
}
