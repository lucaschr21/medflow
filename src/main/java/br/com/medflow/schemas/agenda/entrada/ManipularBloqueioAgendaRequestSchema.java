package br.com.medflow.schemas.agenda.entrada;

import br.com.medflow.entities.agenda.EscopoBloqueioAgenda;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ManipularBloqueioAgendaRequestSchema(
    @NotNull(message = "Início do bloqueio é obrigatório") LocalDateTime inicio,
    @NotNull(message = "Fim do bloqueio é obrigatório") LocalDateTime fim,
    @NotNull(message = "Escopo do bloqueio é obrigatório") EscopoBloqueioAgenda escopo) {}
