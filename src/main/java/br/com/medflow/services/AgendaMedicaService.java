package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.AgendaMedica;
import br.com.medflow.repositories.AgendaMedicaRepository;
import br.com.medflow.schemas.agendamedica.AgendaMedicaInput;
import br.com.medflow.schemas.agendamedica.AgendaMedicaMapper;
import br.com.medflow.schemas.agendamedica.AgendaMedicaOutput;

/**
 * Serviço de domínio para operações de agendas médicas.
 */
@Service
@Transactional(readOnly = true)
public class AgendaMedicaService {

  private final AgendaMedicaRepository agendaMedicaRepository;
  private final AlocacaoMedicoService alocacaoMedicoService;
  private final AgendaMedicaMapper agendaMedicaMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param agendaMedicaRepository repositório de agendas médicas
   * @param alocacaoMedicoService serviço de alocações médicas
   * @param agendaMedicaMapper mapper de agendas médicas
   */
  public AgendaMedicaService(
      AgendaMedicaRepository agendaMedicaRepository,
      AlocacaoMedicoService alocacaoMedicoService,
      AgendaMedicaMapper agendaMedicaMapper) {
    this.agendaMedicaRepository = agendaMedicaRepository;
    this.alocacaoMedicoService = alocacaoMedicoService;
    this.agendaMedicaMapper = agendaMedicaMapper;
  }

  /**
   * Obtém uma agenda médica pelo identificador.
   *
   * @param agendaMedicaId identificador da agenda
   * @return agenda encontrada
   */
  public AgendaMedica findByIdOrThrow(UUID agendaMedicaId) {
    return agendaMedicaRepository.findById(agendaMedicaId)
        .orElseThrow(() -> new EntityNotFoundException("Agenda médica não encontrada: " + agendaMedicaId));
  }

  /**
   * Lista agendas médicas com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de agendas médicas
   */
  public PageResult<AgendaMedicaOutput> findAll(RsqlQuery query, Pageable pageable) {
    return agendaMedicaRepository.findAll(query.toCriteria(pageable)).map(agendaMedicaMapper::toOutput);
  }

  /**
   * Persiste uma nova agenda médica.
   *
   * @param input dados da agenda
   * @return agenda persistida
   */
  @Transactional
  public AgendaMedicaOutput create(AgendaMedicaInput input) {
    AgendaMedica agendaMedica = agendaMedicaMapper.toEntity(input);
    agendaMedica.setAlocacaoMedico(alocacaoMedicoService.findByIdOrThrow(input.alocacaoMedicoId()));
    return agendaMedicaMapper.toOutput(agendaMedicaRepository.save(agendaMedica));
  }

  /**
   * Atualiza os dados de uma agenda médica existente.
   *
   * @param agendaMedicaId identificador da agenda
   * @param input novos dados
   * @return agenda atualizada
   */
  @Transactional
  public AgendaMedicaOutput update(UUID agendaMedicaId, AgendaMedicaInput input) {
    AgendaMedica target = findByIdOrThrow(agendaMedicaId);
    target.setAlocacaoMedico(alocacaoMedicoService.findByIdOrThrow(input.alocacaoMedicoId()));
    agendaMedicaMapper.updateEntity(input, target);
    return agendaMedicaMapper.toOutput(target);
  }

  /**
   * Inativa uma agenda médica existente.
   *
   * @param agendaMedicaId identificador da agenda
   */
  @Transactional
  public void deactivate(UUID agendaMedicaId) {
    agendaMedicaRepository.delete(findByIdOrThrow(agendaMedicaId));
  }
}
