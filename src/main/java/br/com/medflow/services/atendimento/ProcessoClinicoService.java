package br.com.medflow.services.atendimento;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.NotFoundException;
import br.com.medflow.entities.atendimento.ProcessoClinico;
import br.com.medflow.entities.pessoas.Paciente;
import br.com.medflow.repositories.atendimento.ProcessoClinicoRepository;
import br.com.medflow.repositories.pessoas.PacienteRepository;
import br.com.medflow.schemas.atendimento.entrada.ProcessoClinicoPorIdEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.VincularProcessoClinicoEntradaSchema;
import br.com.medflow.schemas.atendimento.mapper.ProcessoClinicoSchemaMapper;
import br.com.medflow.schemas.atendimento.saida.ProcessoClinicoSaidaSchema;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProcessoClinicoService {

  private final ProcessoClinicoRepository processoClinicoRepository;
  private final PacienteRepository pacienteRepository;
  private final ProcessoClinicoSchemaMapper processoClinicoSchemaMapper;

  public ProcessoClinicoSaidaSchema buscarPorIdDaOrganizacao(
      ProcessoClinicoPorIdEntradaSchema entrada) {
    UUID organizacaoId =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    UUID processoClinicoId =
        Objects.requireNonNull(entrada.processoClinicoId(), "Id do processo clínico é obrigatório");
    return processoClinicoSchemaMapper.toSaidaSchema(
        buscarEntidadePorIdDaOrganizacao(organizacaoId, processoClinicoId));
  }

  @Transactional
  public ProcessoClinicoSaidaSchema vincularAoPaciente(
      VincularProcessoClinicoEntradaSchema entrada) {
    UUID organizacaoObrigatoria =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    UUID pacienteId = Objects.requireNonNull(entrada.pacienteId(), "Id do paciente é obrigatório");
    UUID processoClinicoId =
        Objects.requireNonNull(entrada.processoClinicoId(), "Id do processo clínico é obrigatório");

    Paciente paciente =
        pacienteRepository
            .findByIdAndOrganizacaoId(pacienteId, organizacaoObrigatoria)
            .orElseThrow(
                () -> new NotFoundException("Paciente não encontrado na organização informada"));

    ProcessoClinico processoClinico =
        buscarEntidadePorIdDaOrganizacao(organizacaoObrigatoria, processoClinicoId);

    if (processoClinico.getPaciente() != null
        && !Objects.equals(processoClinico.getPaciente().getId(), paciente.getId())) {
      throw new BusinessRuleException("Processo clínico já está vinculado a outro paciente");
    }

    if (!Objects.equals(
        processoClinico.getOrganizacao().getId(), paciente.getOrganizacao().getId())) {
      throw new NotFoundException("Processo clínico não encontrado na organização informada");
    }

    if (paciente.getProcessoClinico() != null
        && !Objects.equals(paciente.getProcessoClinico().getId(), processoClinico.getId())) {
      throw new BusinessRuleException("Paciente já possui outro processo clínico vinculado");
    }

    paciente.definirProcessoClinico(processoClinico);
    pacienteRepository.save(paciente);
    return processoClinicoSchemaMapper.toSaidaSchema(processoClinico);
  }

  private ProcessoClinico buscarEntidadePorIdDaOrganizacao(
      UUID organizacaoId, UUID processoClinicoId) {
    return processoClinicoRepository
        .findByIdAndOrganizacaoId(processoClinicoId, organizacaoId)
        .orElseThrow(
            () ->
                new NotFoundException("Processo clínico não encontrado na organização informada"));
  }
}
