package br.com.medflow.schemas.bloqueioagenda;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.medflow.entities.enums.TipoBloqueioAgenda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para bloqueios de agenda.
 *
 * @param medicoId identificador do médico
 * @param consultorioId identificador do consultório
 * @param inicio data e hora de início
 * @param fim data e hora de fim
 * @param motivo motivo do bloqueio
 * @param tipo tipo do bloqueio
 */
public record BloqueioAgendaInput(
    @NotNull(message = "O médico do bloqueio é obrigatório.")
    UUID medicoId,
    @NotNull(message = "O consultório do bloqueio é obrigatório.")
    UUID consultorioId,
    @NotNull(message = "O início do bloqueio é obrigatório.")
    LocalDateTime inicio,
    @NotNull(message = "O fim do bloqueio é obrigatório.")
    LocalDateTime fim,
    @NotBlank(message = "O motivo do bloqueio é obrigatório.")
    @Size(max = 500, message = "O motivo do bloqueio deve ter no máximo 500 caracteres.")
    String motivo,
    @NotNull(message = "O tipo do bloqueio é obrigatório.")
    TipoBloqueioAgenda tipo) {
}
