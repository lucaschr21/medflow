package br.com.medflow.schemas.agenda.entrada;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AgendaPorIdEntradaSchema(
    @NotNull(message = "Id da organização é obrigatório") UUID organizacaoId,
    @NotNull(message = "Id da agenda é obrigatório") UUID agendaId) {}
