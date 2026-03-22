package br.com.medflow.schemas.atendimento.saida;

import br.com.medflow.entities.atendimento.TipoSanguineo;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ProcessoClinicoSaidaSchema(
    UUID id,
    UUID organizacaoId,
    UUID pacienteId,
    LocalDate dataCriacao,
    TipoSanguineo tipoSangue,
    Set<String> alergias) {}
