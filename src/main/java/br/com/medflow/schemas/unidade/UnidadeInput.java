package br.com.medflow.schemas.unidade;

import java.util.UUID;

import br.com.medflow.schemas.common.EnderecoInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para unidades.
 *
 * @param organizacaoId identificador da organização
 * @param nome nome da unidade
 * @param telefone telefone da unidade
 * @param endereco endereço da unidade
 */
public record UnidadeInput(
    @NotNull(message = "A organização da unidade é obrigatória.")
    UUID organizacaoId,
    @NotBlank(message = "O nome da unidade é obrigatório.")
    @Size(max = 120, message = "O nome da unidade deve ter no máximo 120 caracteres.")
    String nome,
    @NotBlank(message = "O telefone da unidade é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone da unidade deve conter 10 ou 11 dígitos.")
    @Size(min = 10, max = 11, message = "O telefone da unidade deve conter entre 10 e 11 dígitos.")
    String telefone,
    @Valid
    @NotNull(message = "O endereço da unidade é obrigatório.")
    EnderecoInput endereco) {
}
