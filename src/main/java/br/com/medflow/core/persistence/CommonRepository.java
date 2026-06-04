package br.com.medflow.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.QueryCriteria;

/**
 * Contrato base dos repositories JPA da aplicacao.
 *
 * <p>Este contrato preserva as operacoes padrao do Spring Data e adiciona uma
 * abstracao unica para listagens filtradas, paginadas e ordenadas por meio de
 * {@link QueryCriteria}.
 *
 * @param <T> tipo da entidade persistida
 * @param <ID> tipo do identificador da entidade
 * @see QueryCriteria
 * @see PageResult
 */
@NoRepositoryBean
public interface CommonRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

  /**
   * Lista entidades com suporte a filtros, ordenacao e paginacao.
   *
   * <p>Uso tipico:
   *
   * <pre>{@code
   * PageResult<Usuario> page =
   *     usuarioRepository.findAll(rsqlQuery.toCriteria(pageable));
   * }</pre>
   *
   * @param queryCriteria criterios da consulta
   * @return pagina com o resultado da listagem
   */
  PageResult<T> findAll(QueryCriteria<T> queryCriteria);
}
