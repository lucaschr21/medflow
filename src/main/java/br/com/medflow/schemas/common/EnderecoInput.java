package br.com.medflow.schemas.common;

import br.com.medflow.entities.enums.Uf;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para endereços.
 *
 * @param logradouro logradouro do endereço
 * @param numero número do endereço
 * @param bairro bairro do endereço
 * @param cidade cidade do endereço
 * @param uf unidade federativa do endereço
 * @param cep cep do endereço
 * @param complemento complemento do endereço
 */
public record EnderecoInput(
    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 160, message = "O logradouro deve ter no máximo 160 caracteres.")
    String logradouro,
    @NotBlank(message = "O número do endereço é obrigatório.")
    @Size(max = 20, message = "O número do endereço deve ter no máximo 20 caracteres.")
    String numero,
    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 80, message = "O bairro deve ter no máximo 80 caracteres.")
    String bairro,
    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 120, message = "A cidade deve ter no máximo 120 caracteres.")
    String cidade,
    @NotNull(message = "A UF é obrigatória.")
    Uf uf,
    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos.")
    @Size(min = 8, max = 8, message = "O CEP deve conter exatamente 8 dígitos.")
    String cep,
    @Size(max = 120, message = "O complemento deve ter no máximo 120 caracteres.")
    String complemento) {
}
