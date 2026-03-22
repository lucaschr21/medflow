package br.com.medflow.schemas.atendimento.entrada;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CancelarConsultaRequestSchema(
    @NotNull(message = "Motivo do cancelamento é obrigatório")
        @Size(max = 500, message = "Motivo de cancelamento deve ter no máximo 500 caracteres")
        String motivoCancelamento,
    @NotNull(message = "Id do utilizador é obrigatório") UUID utilizadorId) {}
