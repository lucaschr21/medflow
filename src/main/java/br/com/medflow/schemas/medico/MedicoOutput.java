package br.com.medflow.schemas.medico;

import java.util.Set;
import java.util.UUID;

/**
 * Schema de saída para médicos.
 *
 * @param id identificador do médico
 * @param usuarioId identificador do usuário associado
 * @param ativo indicador de atividade
 * @param especialidadeIds identificadores das especialidades associadas
 */
public record MedicoOutput(
    UUID id,
    UUID usuarioId,
    boolean ativo,
    Set<UUID> especialidadeIds) {
}
