package br.com.medflow.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.exceptions.ErrorCode;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.AlocacaoMedico;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.Usuario;
import br.com.medflow.entities.enums.StatusConsulta;
import br.com.medflow.repositories.ConsultaRepository;
import br.com.medflow.schemas.consulta.ConsultaInput;
import br.com.medflow.schemas.consulta.ConsultaMapper;
import br.com.medflow.schemas.consulta.ConsultaOutput;
import br.com.medflow.services.rules.consulta.ConsultaRules;
import br.com.medflow.services.workflow.ConsultaWorkflowService;

/**
 * Serviço de domínio para operações de consultas.
 */
@Service
@Transactional(readOnly = true)
public class ConsultaService {

  private final ConsultaRepository consultaRepository;
  private final UsuarioService usuarioService;
  private final MedicoService medicoService;
  private final ConsultorioService consultorioService;
  private final AlocacaoMedicoService alocacaoMedicoService;
  private final ConsultaMapper consultaMapper;
  private final ConsultaRules consultaRules;
  private final ConsultaWorkflowService workflowService;
  private final DisponibilidadeAgendaService disponibilidadeService;
  private final UsuarioContextService usuarioContextService;

  /**
   * Cria o serviço com suas dependências.
   */
  public ConsultaService(
      ConsultaRepository consultaRepository,
      UsuarioService usuarioService,
      MedicoService medicoService,
      ConsultorioService consultorioService,
      AlocacaoMedicoService alocacaoMedicoService,
      ConsultaMapper consultaMapper,
      ConsultaRules consultaRules,
      ConsultaWorkflowService workflowService,
      DisponibilidadeAgendaService disponibilidadeService,
      UsuarioContextService usuarioContextService) {
    this.consultaRepository = consultaRepository;
    this.usuarioService = usuarioService;
    this.medicoService = medicoService;
    this.consultorioService = consultorioService;
    this.alocacaoMedicoService = alocacaoMedicoService;
    this.consultaMapper = consultaMapper;
    this.consultaRules = consultaRules;
    this.workflowService = workflowService;
    this.disponibilidadeService = disponibilidadeService;
    this.usuarioContextService = usuarioContextService;
  }

  // ---- Operações de leitura ----

  public Consulta findByIdOrThrow(UUID consultaId) {
    return consultaRepository.findById(consultaId)
        .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada: " + consultaId));
  }

  public ConsultaOutput findById(UUID consultaId) {
    return consultaMapper.toOutput(findByIdOrThrow(consultaId));
  }

  public PageResult<ConsultaOutput> findAll(RsqlQuery query, Pageable pageable) {
    return consultaRepository.findAll(query.toCriteria(pageable)).map(consultaMapper::toOutput);
  }

  /**
   * Retorna as consultas do usuário comum autenticado.
   */
  public PageResult<ConsultaOutput> minhasConsultas(Pageable pageable) {
    Usuario usuario = usuarioContextService.getUsuarioOuFalha();
    var page = consultaRepository.findAll((root, query, cb) -> cb.equal(root.get("usuario"), usuario), pageable);
    return PageResult.from(page).map(consultaMapper::toOutput);
  }

  /**
   * Retorna a agenda do médico autenticado para uma data.
   */
  public PageResult<ConsultaOutput> minhaAgenda(LocalDate data, Pageable pageable) {
    Medico medico = usuarioContextService.getMedicoOuFalha();
    var inicio = data.atStartOfDay();
    var fim = data.plusDays(1).atStartOfDay();
    var page = consultaRepository.findAll((root, query, cb) -> cb.and(
        cb.equal(root.get("medico"), medico),
        cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio),
        cb.lessThan(root.get("dataHoraInicio"), fim),
        root.get("status").in(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA,
            StatusConsulta.EM_ESPERA, StatusConsulta.EM_ATENDIMENTO)),
        pageable);
    return PageResult.from(page).map(consultaMapper::toOutput);
  }

  /**
   * Retorna a fila de espera do médico autenticado.
   */
  public PageResult<ConsultaOutput> minhaFila(Pageable pageable) {
    Medico medico = usuarioContextService.getMedicoOuFalha();
    var page = consultaRepository.findAll((root, query, cb) -> cb.and(
        cb.equal(root.get("medico"), medico),
        cb.equal(root.get("status"), StatusConsulta.EM_ESPERA)),
        pageable);
    return PageResult.from(page).map(consultaMapper::toOutput);
  }

  // ---- Operações de escrita ----

  /**
   * Agenda uma consulta com revalidação de disponibilidade.
   */
  @Transactional
  public ConsultaOutput agendar(ConsultaInput input) {
    Usuario usuario = usuarioService.findByIdOrThrow(input.usuarioId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());
    AlocacaoMedico alocacaoMedico = alocacaoMedicoService.findByIdOrThrow(input.alocacaoMedicoId());
    consultaRules.validateAssociations(usuario, medico, consultorio, alocacaoMedico);

    if (!disponibilidadeService.isHorarioDisponivel(medico, consultorio,
        input.dataHoraInicio(), input.dataHoraFim())) {
      throw new BusinessRuleException(
          ErrorCode.HORARIO_INDISPONIVEL,
          "O horário selecionado não está mais disponível.");
    }

    Consulta consulta = consultaMapper.toEntity(input);
    consulta.setUsuario(usuario);
    consulta.setMedico(medico);
    consulta.setConsultorio(consultorio);
    consulta.setAlocacaoMedico(alocacaoMedico);
    consulta.setStatus(StatusConsulta.AGENDADA);
    return consultaMapper.toOutput(consultaRepository.save(consulta));
  }

  /**
   * Realiza o check-in de uma consulta (AGENDADA → CONFIRMADA).
   */
  @Transactional
  public ConsultaOutput checkIn(UUID consultaId) {
    Consulta consulta = findByIdOrThrow(consultaId);
    workflowService.confirmar(consulta);
    return consultaMapper.toOutput(consulta);
  }

  /**
   * Cancela uma consulta.
   */
  @Transactional
  public ConsultaOutput cancelar(UUID consultaId) {
    Consulta consulta = findByIdOrThrow(consultaId);
    workflowService.cancelar(consulta);
    return consultaMapper.toOutput(consulta);
  }

  /**
   * Reagenda uma consulta para novo horário com revalidação.
   */
  @Transactional
  public ConsultaOutput reagendar(UUID consultaId, ConsultaInput input) {
    Consulta consulta = findByIdOrThrow(consultaId);
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());

    if (!disponibilidadeService.isHorarioDisponivel(medico, consultorio,
        input.dataHoraInicio(), input.dataHoraFim())) {
      throw new BusinessRuleException(
          ErrorCode.HORARIO_INDISPONIVEL,
          "O novo horário selecionado não está disponível.");
    }

    consulta.setMedico(medico);
    consulta.setConsultorio(consultorio);
    consultaMapper.updateEntity(input, consulta);
    consulta.setStatus(StatusConsulta.AGENDADA);
    return consultaMapper.toOutput(consulta);
  }

  /**
   * Marca a consulta como não comparecimento.
   */
  @Transactional
  public ConsultaOutput marcarNaoCompareceu(UUID consultaId) {
    Consulta consulta = findByIdOrThrow(consultaId);
    workflowService.marcarNaoCompareceu(consulta);
    return consultaMapper.toOutput(consulta);
  }

  /**
   * Coloca a consulta em espera (CONFIRMADA → EM_ESPERA).
   */
  @Transactional
  public ConsultaOutput colocarEmEspera(UUID consultaId) {
    Consulta consulta = findByIdOrThrow(consultaId);
    workflowService.colocarEmEspera(consulta);
    return consultaMapper.toOutput(consulta);
  }

  /**
   * Inicia o atendimento da consulta (EM_ESPERA → EM_ATENDIMENTO).
   */
  @Transactional
  public ConsultaOutput iniciarAtendimento(UUID consultaId) {
    Consulta consulta = findByIdOrThrow(consultaId);
    Medico medico = usuarioContextService.getMedicoOuFalha();
    if (!consulta.getMedico().getId().equals(medico.getId())) {
      throw new BusinessRuleException(
          ErrorCode.ACCESS_DENIED,
          "Apenas o médico responsável pela consulta pode iniciar o atendimento.");
    }
    workflowService.iniciarAtendimento(consulta);
    return consultaMapper.toOutput(consulta);
  }

  /**
   * Finaliza o atendimento (EM_ATENDIMENTO → FINALIZADA).
   */
  @Transactional
  public ConsultaOutput finalizar(UUID consultaId) {
    Consulta consulta = findByIdOrThrow(consultaId);
    Medico medico = usuarioContextService.getMedicoOuFalha();
    if (!consulta.getMedico().getId().equals(medico.getId())) {
      throw new BusinessRuleException(
          ErrorCode.ACCESS_DENIED,
          "Apenas o médico responsável pela consulta pode finalizar o atendimento.");
    }
    workflowService.finalizar(consulta);
    return consultaMapper.toOutput(consulta);
  }

  // ---- CRUD básico ----

  @Transactional
  public ConsultaOutput create(ConsultaInput input) {
    return agendar(input);
  }

  @Transactional
  public ConsultaOutput update(UUID consultaId, ConsultaInput input) {
    Consulta target = findByIdOrThrow(consultaId);
    Usuario usuario = usuarioService.findByIdOrThrow(input.usuarioId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());
    AlocacaoMedico alocacaoMedico = alocacaoMedicoService.findByIdOrThrow(input.alocacaoMedicoId());
    consultaRules.validateAssociations(usuario, medico, consultorio, alocacaoMedico);
    target.setUsuario(usuario);
    target.setMedico(medico);
    target.setConsultorio(consultorio);
    target.setAlocacaoMedico(alocacaoMedico);
    consultaMapper.updateEntity(input, target);
    return consultaMapper.toOutput(target);
  }

  @Transactional
  public void delete(UUID consultaId) {
    consultaRepository.delete(findByIdOrThrow(consultaId));
  }
}
