package br.com.medflow.schemas.atendimento.entrada;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VincularProcessoClinicoRequestSchema(
    @NotNull(message = "Id do processo clínico é obrigatório") UUID processoClinicoId) {}
