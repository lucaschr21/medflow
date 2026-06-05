package br.com.medflow.schemas.medico;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Schema de entrada para médicos.
 *
 * @param usuarioId identificador do usuário associado
 * @param especialidadeIds identificadores das especialidades associadas
 */
public record MedicoInput(
    @NotNull(message = "O usuário do médico é obrigatório.")
    UUID usuarioId,
    Set<UUID> especialidadeIds) {
}
