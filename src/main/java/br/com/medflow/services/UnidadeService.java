package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Organizacao;
import br.com.medflow.entities.Unidade;
import br.com.medflow.repositories.UnidadeRepository;
import br.com.medflow.schemas.unidade.UnidadeInput;
import br.com.medflow.schemas.unidade.UnidadeMapper;
import br.com.medflow.schemas.unidade.UnidadeOutput;

/**
 * Serviço de domínio para operações de unidades.
 */
@Service
@Transactional(readOnly = true)
public class UnidadeService {

  private final UnidadeRepository unidadeRepository;
  private final OrganizacaoService organizacaoService;
  private final UnidadeMapper unidadeMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param unidadeRepository repositório de unidades
   * @param organizacaoService serviço de organizações
   * @param unidadeMapper mapper de unidades
   */
  public UnidadeService(
      UnidadeRepository unidadeRepository,
      OrganizacaoService organizacaoService,
      UnidadeMapper unidadeMapper) {
    this.unidadeRepository = unidadeRepository;
    this.organizacaoService = organizacaoService;
    this.unidadeMapper = unidadeMapper;
  }

  /**
   * Obtém uma unidade pelo identificador.
   *
   * @param unidadeId identificador da unidade
   * @return unidade encontrada
   */
  public Unidade findByIdOrThrow(UUID unidadeId) {
    return unidadeRepository.findById(unidadeId)
        .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada: " + unidadeId));
  }

  /**
   * Lista unidades com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de unidades
   */
  public PageResult<UnidadeOutput> findAll(RsqlQuery query, Pageable pageable) {
    return unidadeRepository.findAll(query.toCriteria(pageable)).map(unidadeMapper::toOutput);
  }

  /**
   * Persiste uma nova unidade associada a uma organização.
   *
   * @param input dados da unidade
   * @return unidade persistida
   */
  @Transactional
  public UnidadeOutput create(UnidadeInput input) {
    Unidade unidade = unidadeMapper.toEntity(input);
    unidade.setOrganizacao(organizacaoService.findByIdOrThrow(input.organizacaoId()));
    return unidadeMapper.toOutput(unidadeRepository.save(unidade));
  }

  /**
   * Atualiza os dados de uma unidade existente.
   *
   * @param unidadeId identificador da unidade
   * @param input novos dados
   * @return unidade atualizada
   */
  @Transactional
  public UnidadeOutput update(UUID unidadeId, UnidadeInput input) {
    Unidade target = findByIdOrThrow(unidadeId);
    Organizacao organizacao = organizacaoService.findByIdOrThrow(input.organizacaoId());
    target.setOrganizacao(organizacao);
    unidadeMapper.updateEntity(input, target);
    return unidadeMapper.toOutput(target);
  }

  /**
   * Inativa uma unidade existente.
   *
   * @param unidadeId identificador da unidade
   */
  @Transactional
  public void deactivate(UUID unidadeId) {
    unidadeRepository.delete(findByIdOrThrow(unidadeId));
  }
}
