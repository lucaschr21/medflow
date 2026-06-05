package br.com.medflow.schemas.usuario;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Schema de entrada para usuários.
 *
 * @param organizacaoId identificador da organização
 * @param keycloakId identificador do usuário no Keycloak
 */
public record UsuarioInput(
    @NotNull(message = "A organização do usuário é obrigatória.")
    UUID organizacaoId,
    @NotNull(message = "O identificador do usuário no Keycloak é obrigatório.")
    UUID keycloakId) {
}
