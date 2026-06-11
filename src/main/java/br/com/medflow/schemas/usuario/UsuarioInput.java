package br.com.medflow.schemas.usuario;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para criação de usuário.
 *
 * @param organizacaoId  identificador da organização
 * @param username       nome de usuário
 * @param email          e-mail
 * @param firstName      nome
 * @param lastName       sobrenome
 * @param cpf            CPF
 * @param telefone       telefone
 * @param dataNascimento data de nascimento
 * @param tipoAcesso     role/grupo do usuário (ADMINISTRADOR, RECEPCIONISTA,
 *                       MEDICO, USUARIO)
 */
public record UsuarioInput(
                @NotNull(message = "A organização do usuário é obrigatória.") UUID organizacaoId,

                @NotBlank(message = "O nome de usuário é obrigatório.") @Size(max = 80, message = "O nome de usuário deve ter no máximo 80 caracteres.") String username,

                @NotBlank(message = "O e-mail é obrigatório.") @Email(message = "O e-mail informado não é válido.") @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres.") String email,

                @NotBlank(message = "O nome é obrigatório.") @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.") String firstName,

                @NotBlank(message = "O sobrenome é obrigatório.") @Size(max = 120, message = "O sobrenome deve ter no máximo 120 caracteres.") String lastName,

                @NotBlank(message = "O CPF é obrigatório.") @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.") String cpf,

                @NotBlank(message = "O telefone é obrigatório.") @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.") String telefone,

                @NotNull(message = "A data de nascimento é obrigatória.") LocalDate dataNascimento,

                @NotBlank(message = "O tipo de acesso é obrigatório.") String tipoAcesso) {
}
