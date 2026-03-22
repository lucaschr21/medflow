package br.com.medflow.services.atendimento;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.NotFoundException;
import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.atendimento.EstadoConsulta;
import br.com.medflow.entities.estrutura.Consultorio;
import br.com.medflow.entities.financeiro.Convenio;
import br.com.medflow.entities.pessoas.Medico;
import br.com.medflow.entities.pessoas.Paciente;
import br.com.medflow.entities.pessoas.Utilizador;
import br.com.medflow.repositories.atendimento.ConsultaRepository;
import br.com.medflow.repositories.estrutura.ConsultorioMedicoRepository;
import br.com.medflow.repositories.estrutura.ConsultorioRepository;
import br.com.medflow.repositories.financeiro.ConvenioRepository;
import br.com.medflow.repositories.pessoas.AdministradorRepository;
import br.com.medflow.repositories.pessoas.MedicoRepository;
import br.com.medflow.repositories.pessoas.PacienteRepository;
import br.com.medflow.repositories.pessoas.UtilizadorRepository;
import br.com.medflow.schemas.atendimento.entrada.AgendarConsultaEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.CancelarConsultaEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.ConsultaPorIdEntradaSchema;
import br.com.medflow.schemas.atendimento.mapper.ConsultaSchemaMapper;
import br.com.medflow.schemas.atendimento.saida.ConsultaSaidaSchema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultaService {

  private final ConsultaRepository consultaRepository;
  private final ConsultorioMedicoRepository consultorioMedicoRepository;
  private final PacienteRepository pacienteRepository;
  private final MedicoRepository medicoRepository;
  private final ConsultorioRepository consultorioRepository;
  private final ConvenioRepository convenioRepository;
  private final AdministradorRepository administradorRepository;
  private final UtilizadorRepository utilizadorRepository;
  private final ConsultaSchemaMapper consultaSchemaMapper;

  public ConsultaSaidaSchema buscarPorIdDaOrganizacao(ConsultaPorIdEntradaSchema entrada) {
    UUID organizacaoId =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    UUID consultaId = Objects.requireNonNull(entrada.consultaId(), "Id da consulta é obrigatório");
    return consultaSchemaMapper.toSaidaSchema(
        buscarEntidadePorIdDaOrganizacao(organizacaoId, consultaId));
  }

  @Transactional
  public ConsultaSaidaSchema agendarConsulta(AgendarConsultaEntradaSchema entrada) {
    UUID organizacaoObrigatoria =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    LocalDateTime dataHoraObrigatoria =
        Objects.requireNonNull(entrada.dataHoraMarcacao(), "Data/hora de marcação é obrigatória");
    UUID pacienteId = Objects.requireNonNull(entrada.pacienteId(), "Id do paciente é obrigatório");
    UUID medicoId = Objects.requireNonNull(entrada.medicoId(), "Id do médico é obrigatório");
    UUID consultorioId =
        Objects.requireNonNull(entrada.consultorioId(), "Id do consultório é obrigatório");
    UUID convenioId = Objects.requireNonNull(entrada.convenioId(), "Id do convênio é obrigatório");

    Paciente paciente =
        pacienteRepository
            .findByIdAndOrganizacaoId(pacienteId, organizacaoObrigatoria)
            .orElseThrow(
                () -> new NotFoundException("Paciente não encontrado na organização informada"));

    Medico medico =
        medicoRepository
            .findById(medicoId)
            .orElseThrow(() -> new NotFoundException("Médico não encontrado"));

    Consultorio consultorio =
        consultorioRepository
            .findById(consultorioId)
            .orElseThrow(() -> new NotFoundException("Consultório não encontrado"));
    validarConsultorioDaOrganizacao(consultorio, organizacaoObrigatoria);

    Convenio convenio =
        convenioRepository
            .findById(convenioId)
            .orElseThrow(() -> new NotFoundException("Convênio não encontrado"));
    validarConvenioAtivoDaOrganizacao(convenio, organizacaoObrigatoria);

    validarVinculoMedicoConsultorioAtivo(
        organizacaoObrigatoria,
        medico.getId(),
        consultorio.getId(),
        dataHoraObrigatoria.toLocalDate());

    if (!medicoDisponivel(medico.getId(), dataHoraObrigatoria)) {
      throw new BusinessRuleException("Médico já possui consulta ativa nesse horário");
    }
    if (!consultorioDisponivel(consultorio.getId(), dataHoraObrigatoria)) {
      throw new BusinessRuleException("Consultório já possui consulta ativa nesse horário");
    }

    Consulta consulta = new Consulta();
    consulta.definirPaciente(paciente);
    consulta.definirMedico(medico);
    consulta.definirConsultorio(consultorio);
    consulta.definirConvenio(convenio);
    consulta.definirDataHoraMarcacao(dataHoraObrigatoria);
    consulta.definirTipoConsulta(entrada.tipoConsulta());

    return consultaSchemaMapper.toSaidaSchema(consultaRepository.save(consulta));
  }

  @Transactional
  public ConsultaSaidaSchema cancelarConsulta(CancelarConsultaEntradaSchema entrada) {
    UUID organizacaoId =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    UUID consultaId = Objects.requireNonNull(entrada.consultaId(), "Id da consulta é obrigatório");
    UUID utilizadorId =
        Objects.requireNonNull(entrada.utilizadorId(), "Id do utilizador é obrigatório");

    Consulta consulta = buscarEntidadePorIdDaOrganizacao(organizacaoId, consultaId);

    if (consulta.getEstado() == EstadoConsulta.CANCELADA) {
      throw new BusinessRuleException("Consulta já está cancelada");
    }

    Utilizador utilizadorResponsavel =
        utilizadorRepository
            .findById(utilizadorId)
            .orElseThrow(() -> new NotFoundException("Utilizador não encontrado"));

    validarAutorizacaoCancelamento(consulta, utilizadorResponsavel);

    consulta.cancelar(entrada.motivoCancelamento(), utilizadorResponsavel);
    return consultaSchemaMapper.toSaidaSchema(consultaRepository.save(consulta));
  }

  private Consulta buscarEntidadePorIdDaOrganizacao(UUID organizacaoId, UUID consultaId) {
    return consultaRepository
        .findByIdAndPacienteOrganizacaoId(consultaId, organizacaoId)
        .orElseThrow(
            () -> new NotFoundException("Consulta não encontrada na organização informada"));
  }

  private boolean medicoDisponivel(UUID medicoId, LocalDateTime dataHoraMarcacao) {
    return !consultaRepository.existsByMedicoIdAndDataHoraMarcacaoAndEstadoNot(
        medicoId, dataHoraMarcacao, EstadoConsulta.CANCELADA);
  }

  private boolean consultorioDisponivel(UUID consultorioId, LocalDateTime dataHoraMarcacao) {
    return !consultaRepository.existsByConsultorioIdAndDataHoraMarcacaoAndEstadoNot(
        consultorioId, dataHoraMarcacao, EstadoConsulta.CANCELADA);
  }

  private void validarVinculoMedicoConsultorioAtivo(
      UUID organizacaoId, UUID medicoId, UUID consultorioId, LocalDate dataReferencia) {
    var vinculo =
        consultorioMedicoRepository
            .findByConsultorioIdAndMedicoId(consultorioId, medicoId)
            .orElseThrow(
                () ->
                    new BusinessRuleException(
                        "Médico não possui vínculo com o consultório informado"));

    UUID organizacaoDoVinculo = vinculo.getConsultorio().getUnidade().getOrganizacao().getId();
    if (!Objects.equals(organizacaoDoVinculo, organizacaoId)) {
      throw new NotFoundException("Consultório não encontrado na organização informada");
    }

    if (!vinculo.estaVinculoAtivoEm(dataReferencia)) {
      throw new BusinessRuleException(
          "Médico não possui vínculo ativo com o consultório na data informada");
    }
  }

  private void validarAutorizacaoCancelamento(Consulta consulta, Utilizador utilizadorResponsavel) {
    UUID idUtilizadorResponsavel = utilizadorResponsavel.getId();
    UUID idUtilizadorPaciente = consulta.getPaciente().getUtilizador().getId();
    UUID idUtilizadorMedico = consulta.getMedico().getUtilizador().getId();
    UUID idOrganizacao = consulta.getPaciente().getOrganizacao().getId();

    boolean ePacienteDaConsulta = Objects.equals(idUtilizadorResponsavel, idUtilizadorPaciente);
    boolean eMedicoDaConsulta = Objects.equals(idUtilizadorResponsavel, idUtilizadorMedico);
    boolean eAdministradorDaOrganizacao =
        administradorRepository.existsByUtilizadorIdAndOrganizacaoId(
            idUtilizadorResponsavel, idOrganizacao);

    if (!ePacienteDaConsulta && !eMedicoDaConsulta && !eAdministradorDaOrganizacao) {
      throw new BusinessRuleException(
          "Utilizador informado não possui permissão para cancelar a consulta");
    }
  }

  private void validarConsultorioDaOrganizacao(Consultorio consultorio, UUID organizacaoId) {
    UUID organizacaoDoConsultorio = consultorio.getUnidade().getOrganizacao().getId();
    if (!Objects.equals(organizacaoDoConsultorio, organizacaoId)) {
      throw new NotFoundException("Consultório não encontrado na organização informada");
    }
  }

  private void validarConvenioAtivoDaOrganizacao(Convenio convenio, UUID organizacaoId) {
    if (!Objects.equals(convenio.getOrganizacao().getId(), organizacaoId)) {
      throw new NotFoundException("Convênio não encontrado na organização informada");
    }
    if (!Boolean.TRUE.equals(convenio.getAtivo())) {
      throw new BusinessRuleException("Convênio informado está inativo");
    }
  }
}
