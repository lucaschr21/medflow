package br.com.medflow.schemas.consultorio;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para consultórios.
 *
 * @param unidadeId identificador da unidade
 * @param nome nome do consultório
 * @param sala sala do consultório
 */
public record ConsultorioInput(
    @NotNull(message = "A unidade do consultório é obrigatória.")
    UUID unidadeId,
    @NotBlank(message = "O nome do consultório é obrigatório.")
    @Size(max = 120, message = "O nome do consultório deve ter no máximo 120 caracteres.")
    String nome,
    @NotBlank(message = "A sala do consultório é obrigatória.")
    @Size(max = 40, message = "A sala do consultório deve ter no máximo 40 caracteres.")
    String sala) {
}
