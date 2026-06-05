package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.BloqueioAgenda;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;
import br.com.medflow.repositories.BloqueioAgendaRepository;
import br.com.medflow.schemas.bloqueioagenda.BloqueioAgendaInput;
import br.com.medflow.schemas.bloqueioagenda.BloqueioAgendaMapper;
import br.com.medflow.schemas.bloqueioagenda.BloqueioAgendaOutput;

/**
 * Serviço de domínio para operações de bloqueios de agenda.
 */
@Service
@Transactional(readOnly = true)
public class BloqueioAgendaService {

  private final BloqueioAgendaRepository bloqueioAgendaRepository;
  private final MedicoService medicoService;
  private final ConsultorioService consultorioService;
  private final BloqueioAgendaMapper bloqueioAgendaMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param bloqueioAgendaRepository repositório de bloqueios de agenda
   * @param medicoService serviço de médicos
   * @param consultorioService serviço de consultórios
   * @param bloqueioAgendaMapper mapper de bloqueios de agenda
   */
  public BloqueioAgendaService(
      BloqueioAgendaRepository bloqueioAgendaRepository,
      MedicoService medicoService,
      ConsultorioService consultorioService,
      BloqueioAgendaMapper bloqueioAgendaMapper) {
    this.bloqueioAgendaRepository = bloqueioAgendaRepository;
    this.medicoService = medicoService;
    this.consultorioService = consultorioService;
    this.bloqueioAgendaMapper = bloqueioAgendaMapper;
  }

  /**
   * Obtém um bloqueio de agenda pelo identificador.
   *
   * @param bloqueioAgendaId identificador do bloqueio
   * @return bloqueio encontrado
   */
  public BloqueioAgenda findByIdOrThrow(UUID bloqueioAgendaId) {
    return bloqueioAgendaRepository.findById(bloqueioAgendaId)
        .orElseThrow(() -> new EntityNotFoundException("Bloqueio de agenda não encontrado: " + bloqueioAgendaId));
  }

  /**
   * Lista bloqueios de agenda com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de bloqueios de agenda
   */
  public PageResult<BloqueioAgendaOutput> findAll(RsqlQuery query, Pageable pageable) {
    return bloqueioAgendaRepository.findAll(query.toCriteria(pageable)).map(bloqueioAgendaMapper::toOutput);
  }

  /**
   * Persiste um novo bloqueio de agenda.
   *
   * @param input dados do bloqueio
   * @return bloqueio persistido
   */
  @Transactional
  public BloqueioAgendaOutput create(BloqueioAgendaInput input) {
    BloqueioAgenda bloqueioAgenda = bloqueioAgendaMapper.toEntity(input);
    bloqueioAgenda.setMedico(medicoService.findByIdOrThrow(input.medicoId()));
    bloqueioAgenda.setConsultorio(consultorioService.findByIdOrThrow(input.consultorioId()));
    return bloqueioAgendaMapper.toOutput(bloqueioAgendaRepository.save(bloqueioAgenda));
  }

  /**
   * Atualiza os dados de um bloqueio de agenda existente.
   *
   * @param bloqueioAgendaId identificador do bloqueio
   * @param input novos dados
   * @return bloqueio atualizado
   */
  @Transactional
  public BloqueioAgendaOutput update(UUID bloqueioAgendaId, BloqueioAgendaInput input) {
    BloqueioAgenda target = findByIdOrThrow(bloqueioAgendaId);
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());
    target.setMedico(medico);
    target.setConsultorio(consultorio);
    bloqueioAgendaMapper.updateEntity(input, target);
    return bloqueioAgendaMapper.toOutput(target);
  }

  /**
   * Remove um bloqueio de agenda existente.
   *
   * @param bloqueioAgendaId identificador do bloqueio
   */
  @Transactional
  public void delete(UUID bloqueioAgendaId) {
    bloqueioAgendaRepository.delete(findByIdOrThrow(bloqueioAgendaId));
  }
}
