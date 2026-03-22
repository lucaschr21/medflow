package br.com.medflow.schemas.pessoas.saida;

import java.time.LocalDate;
import java.util.UUID;

public record PacienteSaidaSchema(
    UUID id,
    UUID organizacaoId,
    UUID utilizadorId,
    LocalDate dataNascimento,
    String nif,
    String numeroBeneficiario,
    UUID processoClinicoId) {}
