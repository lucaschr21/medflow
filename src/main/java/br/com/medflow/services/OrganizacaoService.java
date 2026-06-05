package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Organizacao;
import br.com.medflow.repositories.OrganizacaoRepository;
import br.com.medflow.schemas.organizacao.OrganizacaoInput;
import br.com.medflow.schemas.organizacao.OrganizacaoMapper;
import br.com.medflow.schemas.organizacao.OrganizacaoOutput;

/**
 * Serviço de domínio para operações de organizações.
 */
@Service
@Transactional(readOnly = true)
public class OrganizacaoService {

  private final OrganizacaoRepository organizacaoRepository;
  private final OrganizacaoMapper organizacaoMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param organizacaoRepository repositório de organizações
   * @param organizacaoMapper mapper de organizações
   */
  public OrganizacaoService(
      OrganizacaoRepository organizacaoRepository,
      OrganizacaoMapper organizacaoMapper) {
    this.organizacaoRepository = organizacaoRepository;
    this.organizacaoMapper = organizacaoMapper;
  }

  /**
   * Obtém uma organização pelo identificador.
   *
   * @param organizacaoId identificador da organização
   * @return organização encontrada
   */
  public Organizacao findByIdOrThrow(UUID organizacaoId) {
    return organizacaoRepository.findById(organizacaoId)
        .orElseThrow(() -> new EntityNotFoundException("Organização não encontrada: " + organizacaoId));
  }

  /**
   * Lista organizações com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de organizações
   */
  public PageResult<OrganizacaoOutput> findAll(RsqlQuery query, Pageable pageable) {
    return organizacaoRepository.findAll(query.toCriteria(pageable)).map(organizacaoMapper::toOutput);
  }

  /**
   * Persiste uma nova organização.
   *
   * @param input dados da organização
   * @return organização persistida
   */
  @Transactional
  public OrganizacaoOutput create(OrganizacaoInput input) {
    Organizacao organizacao = organizacaoMapper.toEntity(input);
    return organizacaoMapper.toOutput(organizacaoRepository.save(organizacao));
  }

  /**
   * Atualiza os dados de uma organização existente.
   *
   * @param organizacaoId identificador da organização
   * @param input novos dados
   * @return organização atualizada
   */
  @Transactional
  public OrganizacaoOutput update(UUID organizacaoId, OrganizacaoInput input) {
    Organizacao target = findByIdOrThrow(organizacaoId);
    organizacaoMapper.updateEntity(input, target);
    return organizacaoMapper.toOutput(target);
  }

  /**
   * Inativa uma organização existente.
   *
   * @param organizacaoId identificador da organização
   */
  @Transactional
  public void deactivate(UUID organizacaoId) {
    organizacaoRepository.delete(findByIdOrThrow(organizacaoId));
  }
}
