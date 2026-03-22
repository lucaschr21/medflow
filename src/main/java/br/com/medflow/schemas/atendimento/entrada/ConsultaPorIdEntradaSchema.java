package br.com.medflow.schemas.atendimento.entrada;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ConsultaPorIdEntradaSchema(
    @NotNull(message = "Id da organização é obrigatório") UUID organizacaoId,
    @NotNull(message = "Id da consulta é obrigatório") UUID consultaId) {}
