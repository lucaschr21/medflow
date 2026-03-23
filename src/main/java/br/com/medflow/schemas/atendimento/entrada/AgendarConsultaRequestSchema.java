package br.com.medflow.schemas.atendimento.entrada;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

public record AgendarConsultaRequestSchema(
    @NotNull(message = "Id do paciente é obrigatório") UUID pacienteId,
    @NotNull(message = "Id do médico é obrigatório") UUID medicoId,
    @NotNull(message = "Id do consultório é obrigatório") UUID consultorioId,
    @NotNull(message = "Id do convênio é obrigatório") UUID convenioId,
    @NotNull(message = "Data/hora da consulta é obrigatória") LocalDateTime dataHoraMarcacao,
    @Size(max = 80, message = "Tipo de consulta deve ter no máximo 80 caracteres")
        String tipoConsulta) {}
