package br.com.medflow.schemas.pessoas.entrada;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PacientePorIdEntradaSchema(
    @NotNull(message = "Id da organização é obrigatório") UUID organizacaoId,
    @NotNull(message = "Id do paciente é obrigatório") UUID pacienteId) {}
