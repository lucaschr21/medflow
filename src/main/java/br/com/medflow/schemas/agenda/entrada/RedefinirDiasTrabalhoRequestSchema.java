package br.com.medflow.schemas.agenda.entrada;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.Set;

public record RedefinirDiasTrabalhoRequestSchema(
    @NotNull(message = "Dias de trabalho são obrigatórios") Set<DayOfWeek> diasTrabalho) {}
