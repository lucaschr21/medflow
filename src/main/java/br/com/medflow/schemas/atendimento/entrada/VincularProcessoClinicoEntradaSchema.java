package br.com.medflow.schemas.atendimento.entrada;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VincularProcessoClinicoEntradaSchema(
    @NotNull(message = "Id da organização é obrigatório") UUID organizacaoId,
    @NotNull(message = "Id do paciente é obrigatório") UUID pacienteId,
    @NotNull(message = "Id do processo clínico é obrigatório") UUID processoClinicoId) {}
