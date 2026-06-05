package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Unidade;
import br.com.medflow.repositories.ConsultorioRepository;
import br.com.medflow.schemas.consultorio.ConsultorioInput;
import br.com.medflow.schemas.consultorio.ConsultorioMapper;
import br.com.medflow.schemas.consultorio.ConsultorioOutput;

/**
 * Serviço de domínio para operações de consultórios.
 */
@Service
@Transactional(readOnly = true)
public class ConsultorioService {

  private final ConsultorioRepository consultorioRepository;
  private final UnidadeService unidadeService;
  private final ConsultorioMapper consultorioMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param consultorioRepository repositório de consultórios
   * @param unidadeService serviço de unidades
   * @param consultorioMapper mapper de consultórios
   */
  public ConsultorioService(
      ConsultorioRepository consultorioRepository,
      UnidadeService unidadeService,
      ConsultorioMapper consultorioMapper) {
    this.consultorioRepository = consultorioRepository;
    this.unidadeService = unidadeService;
    this.consultorioMapper = consultorioMapper;
  }

  /**
   * Obtém um consultório pelo identificador.
   *
   * @param consultorioId identificador do consultório
   * @return consultório encontrado
   */
  public Consultorio findByIdOrThrow(UUID consultorioId) {
    return consultorioRepository.findById(consultorioId)
        .orElseThrow(() -> new EntityNotFoundException("Consultório não encontrado: " + consultorioId));
  }

  /**
   * Lista consultórios com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de consultórios
   */
  public PageResult<ConsultorioOutput> findAll(RsqlQuery query, Pageable pageable) {
    return consultorioRepository.findAll(query.toCriteria(pageable)).map(consultorioMapper::toOutput);
  }

  /**
   * Persiste um novo consultório associado a uma unidade.
   *
   * @param input dados do consultório
   * @return consultório persistido
   */
  @Transactional
  public ConsultorioOutput create(ConsultorioInput input) {
    Consultorio consultorio = consultorioMapper.toEntity(input);
    consultorio.setUnidade(unidadeService.findByIdOrThrow(input.unidadeId()));
    return consultorioMapper.toOutput(consultorioRepository.save(consultorio));
  }

  /**
   * Atualiza os dados de um consultório existente.
   *
   * @param consultorioId identificador do consultório
   * @param input novos dados
   * @return consultório atualizado
   */
  @Transactional
  public ConsultorioOutput update(UUID consultorioId, ConsultorioInput input) {
    Consultorio target = findByIdOrThrow(consultorioId);
    Unidade unidade = unidadeService.findByIdOrThrow(input.unidadeId());
    target.setUnidade(unidade);
    consultorioMapper.updateEntity(input, target);
    return consultorioMapper.toOutput(target);
  }

  /**
   * Inativa um consultório existente.
   *
   * @param consultorioId identificador do consultório
   */
  @Transactional
  public void deactivate(UUID consultorioId) {
    consultorioRepository.delete(findByIdOrThrow(consultorioId));
  }
}
