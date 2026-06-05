package br.com.medflow.schemas.usuario;

import java.util.UUID;

/**
 * Schema de saída para usuários.
 *
 * @param id identificador do usuário
 * @param organizacaoId identificador da organização
 * @param keycloakId identificador do usuário no Keycloak
 * @param ativo indicador de atividade
 * @param medicoId identificador do médico associado, quando existir
 */
public record UsuarioOutput(
    UUID id,
    UUID organizacaoId,
    UUID keycloakId,
    boolean ativo,
    UUID medicoId) {
}
