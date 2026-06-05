package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.AlocacaoMedico;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.Usuario;
import br.com.medflow.repositories.ConsultaRepository;
import br.com.medflow.schemas.consulta.ConsultaInput;
import br.com.medflow.schemas.consulta.ConsultaMapper;
import br.com.medflow.schemas.consulta.ConsultaOutput;
import br.com.medflow.services.rules.consulta.ConsultaRules;

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

  /**
   * Cria o serviço com suas dependências.
   *
   * @param consultaRepository repositório de consultas
   * @param usuarioService serviço de usuários
   * @param medicoService serviço de médicos
   * @param consultorioService serviço de consultórios
   * @param alocacaoMedicoService serviço de alocações médicas
   * @param consultaMapper mapper de consultas
   * @param consultaRules regras de consultas
   */
  public ConsultaService(
      ConsultaRepository consultaRepository,
      UsuarioService usuarioService,
      MedicoService medicoService,
      ConsultorioService consultorioService,
      AlocacaoMedicoService alocacaoMedicoService,
      ConsultaMapper consultaMapper,
      ConsultaRules consultaRules) {
    this.consultaRepository = consultaRepository;
    this.usuarioService = usuarioService;
    this.medicoService = medicoService;
    this.consultorioService = consultorioService;
    this.alocacaoMedicoService = alocacaoMedicoService;
    this.consultaMapper = consultaMapper;
    this.consultaRules = consultaRules;
  }

  /**
   * Obtém uma consulta pelo identificador.
   *
   * @param consultaId identificador da consulta
   * @return consulta encontrada
   */
  public Consulta findByIdOrThrow(UUID consultaId) {
    return consultaRepository.findById(consultaId)
        .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada: " + consultaId));
  }

  /**
   * Lista consultas com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de consultas
   */
  public PageResult<ConsultaOutput> findAll(RsqlQuery query, Pageable pageable) {
    return consultaRepository.findAll(query.toCriteria(pageable)).map(consultaMapper::toOutput);
  }

  /**
   * Persiste uma nova consulta.
   *
   * @param input dados da consulta
   * @return consulta persistida
   */
  @Transactional
  public ConsultaOutput create(ConsultaInput input) {
    Consulta consulta = consultaMapper.toEntity(input);
    Usuario usuario = usuarioService.findByIdOrThrow(input.usuarioId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());
    AlocacaoMedico alocacaoMedico = alocacaoMedicoService.findByIdOrThrow(input.alocacaoMedicoId());
    consultaRules.validateAssociations(usuario, medico, consultorio, alocacaoMedico);
    consulta.setUsuario(usuario);
    consulta.setMedico(medico);
    consulta.setConsultorio(consultorio);
    consulta.setAlocacaoMedico(alocacaoMedico);
    return consultaMapper.toOutput(consultaRepository.save(consulta));
  }

  /**
   * Atualiza os dados de uma consulta existente.
   *
   * @param consultaId identificador da consulta
   * @param input novos dados
   * @return consulta atualizada
   */
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

  /**
   * Remove uma consulta existente.
   *
   * @param consultaId identificador da consulta
   */
  @Transactional
  public void delete(UUID consultaId) {
    consultaRepository.delete(findByIdOrThrow(consultaId));
  }
}
