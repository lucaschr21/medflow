package br.com.medflow.schemas.atendimento.saida;

import br.com.medflow.entities.atendimento.EstadoConsulta;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaSaidaSchema(
    UUID id,
    UUID organizacaoId,
    UUID pacienteId,
    UUID medicoId,
    UUID consultorioId,
    UUID convenioId,
    LocalDateTime dataHoraMarcacao,
    EstadoConsulta estado,
    String tipoConsulta,
    String motivoCancelamento,
    LocalDateTime canceladoEm,
    UUID canceladoPorUtilizadorId) {}
