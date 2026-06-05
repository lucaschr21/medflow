package br.com.medflow.schemas.bloqueioagenda;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.medflow.entities.enums.TipoBloqueioAgenda;

/**
 * Schema de saída para bloqueios de agenda.
 *
 * @param id identificador do bloqueio
 * @param medicoId identificador do médico
 * @param consultorioId identificador do consultório
 * @param inicio data e hora de início
 * @param fim data e hora de fim
 * @param motivo motivo do bloqueio
 * @param tipo tipo do bloqueio
 */
public record BloqueioAgendaOutput(
    UUID id,
    UUID medicoId,
    UUID consultorioId,
    LocalDateTime inicio,
    LocalDateTime fim,
    String motivo,
    TipoBloqueioAgenda tipo) {
}
