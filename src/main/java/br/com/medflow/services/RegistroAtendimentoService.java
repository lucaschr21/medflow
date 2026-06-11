package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.exceptions.ErrorCode;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.RegistroAtendimento;
import br.com.medflow.entities.enums.StatusConsulta;
import br.com.medflow.repositories.RegistroAtendimentoRepository;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoInput;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoMapper;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoOutput;
import br.com.medflow.services.rules.registroatendimento.RegistroAtendimentoRules;

/**
 * Serviço de domínio para operações de registros de atendimento.
 */
@Service
@Transactional(readOnly = true)
public class RegistroAtendimentoService {

  private final RegistroAtendimentoRepository registroAtendimentoRepository;
  private final ConsultaService consultaService;
  private final MedicoService medicoService;
  private final RegistroAtendimentoMapper registroAtendimentoMapper;
  private final RegistroAtendimentoRules registroAtendimentoRules;
  private final UsuarioContextService usuarioContextService;

  public RegistroAtendimentoService(
      RegistroAtendimentoRepository registroAtendimentoRepository,
      ConsultaService consultaService,
      MedicoService medicoService,
      RegistroAtendimentoMapper registroAtendimentoMapper,
      RegistroAtendimentoRules registroAtendimentoRules,
      UsuarioContextService usuarioContextService) {
    this.registroAtendimentoRepository = registroAtendimentoRepository;
    this.consultaService = consultaService;
    this.medicoService = medicoService;
    this.registroAtendimentoMapper = registroAtendimentoMapper;
    this.registroAtendimentoRules = registroAtendimentoRules;
    this.usuarioContextService = usuarioContextService;
  }

  // ---- Operações de leitura ----

  public RegistroAtendimento findByIdOrThrow(UUID registroAtendimentoId) {
    return registroAtendimentoRepository.findById(registroAtendimentoId).orElseThrow(
        () -> new EntityNotFoundException("Registro de atendimento não encontrado: " + registroAtendimentoId));
  }

  public RegistroAtendimentoOutput findById(UUID registroAtendimentoId) {
    return registroAtendimentoMapper.toOutput(findByIdOrThrow(registroAtendimentoId));
  }

  public PageResult<RegistroAtendimentoOutput> findAll(RsqlQuery query, Pageable pageable) {
    return registroAtendimentoRepository.findAll(query.toCriteria(pageable))
        .map(registroAtendimentoMapper::toOutput);
  }

  // ---- Operações operacionais ----

  /**
   * Cria registro de atendimento para uma consulta.
   * Apenas o médico da consulta autenticado pode registrar.
   */
  @Transactional
  public RegistroAtendimentoOutput criarRegistro(UUID consultaId, RegistroAtendimentoInput input) {
    Consulta consulta = consultaService.findByIdOrThrow(consultaId);
    Medico medicoAutenticado = usuarioContextService.getMedicoOuFalha();

    if (!consulta.getMedico().getId().equals(medicoAutenticado.getId())) {
      throw new BusinessRuleException(
          ErrorCode.REGISTRO_MEDICO_INVALIDO,
          "Apenas o médico responsável pela consulta pode registrar o atendimento.");
    }
    if (consulta.getStatus() != StatusConsulta.EM_ATENDIMENTO) {
      throw new BusinessRuleException(
          ErrorCode.REGISTRO_CONSULTA_NAO_EM_ATENDIMENTO,
          "O registro de atendimento requer que a consulta esteja em atendimento.");
    }

    RegistroAtendimento registro = registroAtendimentoMapper.toEntity(input);
    registro.setConsulta(consulta);
    registro.setMedico(medicoAutenticado);
    return registroAtendimentoMapper.toOutput(registroAtendimentoRepository.save(registro));
  }

  /**
   * Atualiza registro de atendimento existente.
   * Apenas o médico da consulta autenticado pode atualizar.
   */
  @Transactional
  public RegistroAtendimentoOutput atualizarRegistro(UUID consultaId, UUID registroId, RegistroAtendimentoInput input) {
    RegistroAtendimento target = findByIdOrThrow(registroId);
    Medico medicoAutenticado = usuarioContextService.getMedicoOuFalha();

    if (!target.getConsulta().getId().equals(consultaId)) {
      throw new BusinessRuleException(
          ErrorCode.VALIDATION_ERROR,
          "O registro de atendimento não pertence à consulta informada.");
    }
    if (!target.getMedico().getId().equals(medicoAutenticado.getId())) {
      throw new BusinessRuleException(
          ErrorCode.REGISTRO_MEDICO_INVALIDO,
          "Apenas o médico responsável pode alterar o registro de atendimento.");
    }

    registroAtendimentoMapper.updateEntity(input, target);
    return registroAtendimentoMapper.toOutput(target);
  }

  // ---- CRUD básico ----

  @Transactional
  public RegistroAtendimentoOutput create(RegistroAtendimentoInput input) {
    RegistroAtendimento registroAtendimento = registroAtendimentoMapper.toEntity(input);
    Consulta consulta = consultaService.findByIdOrThrow(input.consultaId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    registroAtendimentoRules.validateAssociations(consulta, medico);
    registroAtendimento.setConsulta(consulta);
    registroAtendimento.setMedico(medico);
    return registroAtendimentoMapper.toOutput(registroAtendimentoRepository.save(registroAtendimento));
  }

  @Transactional
  public RegistroAtendimentoOutput update(UUID registroAtendimentoId, RegistroAtendimentoInput input) {
    RegistroAtendimento target = findByIdOrThrow(registroAtendimentoId);
    Consulta consulta = consultaService.findByIdOrThrow(input.consultaId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    registroAtendimentoRules.validateAssociations(consulta, medico);
    target.setConsulta(consulta);
    target.setMedico(medico);
    registroAtendimentoMapper.updateEntity(input, target);
    return registroAtendimentoMapper.toOutput(target);
  }

  @Transactional
  public void delete(UUID registroAtendimentoId) {
    registroAtendimentoRepository.delete(findByIdOrThrow(registroAtendimentoId));
  }
}
