package br.com.medflow.schemas.agenda.entrada;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.Set;

public record RedefinirDiasTrabalhoEntradaSchema(
    @NotNull(message = "Dados da agenda são obrigatórios") AgendaPorIdEntradaSchema agenda,
    @NotNull(message = "Dias de trabalho são obrigatórios") Set<DayOfWeek> diasTrabalho) {}
