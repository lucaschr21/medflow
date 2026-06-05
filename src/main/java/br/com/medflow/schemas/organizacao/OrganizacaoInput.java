package br.com.medflow.schemas.organizacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para organizações.
 *
 * @param nome nome da organização
 * @param email e-mail da organização
 * @param telefone telefone da organização
 * @param logotipo logotipo da organização
 * @param logotipoContentType content type do logotipo
 * @param corPrimaria cor primária da organização
 * @param ativo indicador de atividade
 */
public record OrganizacaoInput(
    @NotBlank(message = "O nome da organização é obrigatório.")
    @Size(max = 120, message = "O nome da organização deve ter no máximo 120 caracteres.")
    String nome,
    @NotBlank(message = "O e-mail da organização é obrigatório.")
    @Email(message = "O e-mail da organização deve ser válido.")
    @Size(max = 254, message = "O e-mail da organização deve ter no máximo 254 caracteres.")
    String email,
    @NotBlank(message = "O telefone da organização é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone da organização deve conter 10 ou 11 dígitos.")
    @Size(min = 10, max = 11, message = "O telefone da organização deve conter entre 10 e 11 dígitos.")
    String telefone,
    byte[] logotipo,
    @Size(max = 100, message = "O content type do logotipo deve ter no máximo 100 caracteres.")
    String logotipoContentType,
    @NotBlank(message = "A cor primária da organização é obrigatória.")
    @Pattern(regexp = "#[0-9A-Fa-f]{6}", message = "A cor primária deve estar no formato hexadecimal #RRGGBB.")
    String corPrimaria,
    boolean ativo) {
}
