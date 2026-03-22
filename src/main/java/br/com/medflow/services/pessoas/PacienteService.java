package br.com.medflow.services.pessoas;

import br.com.medflow.core.exceptions.NotFoundException;
import br.com.medflow.entities.pessoas.Paciente;
import br.com.medflow.repositories.pessoas.PacienteRepository;
import br.com.medflow.schemas.pessoas.entrada.PacientePorIdEntradaSchema;
import br.com.medflow.schemas.pessoas.mapper.PacienteSchemaMapper;
import br.com.medflow.schemas.pessoas.saida.PacienteSaidaSchema;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PacienteService {

  private final PacienteRepository pacienteRepository;
  private final PacienteSchemaMapper pacienteSchemaMapper;

  public PacienteSaidaSchema buscarPorIdDaOrganizacao(PacientePorIdEntradaSchema entrada) {
    UUID organizacaoId =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    UUID pacienteId = Objects.requireNonNull(entrada.pacienteId(), "Id do paciente é obrigatório");
    return pacienteSchemaMapper.toSaidaSchema(
        buscarEntidadePorIdDaOrganizacao(organizacaoId, pacienteId));
  }

  private Paciente buscarEntidadePorIdDaOrganizacao(UUID organizacaoId, UUID pacienteId) {
    return pacienteRepository
        .findByIdAndOrganizacaoId(pacienteId, organizacaoId)
        .orElseThrow(
            () -> new NotFoundException("Paciente não encontrado na organização informada"));
  }
}
