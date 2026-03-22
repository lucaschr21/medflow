package br.com.medflow.schemas.agenda.saida;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;

public record AgendaSaidaSchema(
    UUID id,
    UUID organizacaoId,
    UUID consultorioMedicoId,
    Set<DayOfWeek> diasTrabalho,
    int quantidadeBloqueios) {}
