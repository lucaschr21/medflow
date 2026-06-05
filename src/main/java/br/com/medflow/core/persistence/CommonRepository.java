package br.com.medflow.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.QueryCriteria;

/**
 * Contrato base dos repositories JPA da aplicação.
 *
 * <p>Este contrato preserva as operações padrão do Spring Data e adiciona uma
 * abstração única para listagens filtradas, paginadas e ordenadas por meio de
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
   * Lista entidades com suporte a filtros, ordenação e paginação.
   *
   * <p>Uso tipico:
   *
   * <pre>{@code
   * PageResult<Usuario> page =
   *     usuarioRepository.findAll(rsqlQuery.toCriteria(pageable));
   * }</pre>
   *
   * @param queryCriteria critérios da consulta
   * @return página com o resultado da listagem
   */
  PageResult<T> findAll(QueryCriteria<T> queryCriteria);
}
