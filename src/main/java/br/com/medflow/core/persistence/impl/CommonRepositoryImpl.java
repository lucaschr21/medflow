package br.com.medflow.core.persistence.impl;

import java.io.Serializable;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.QueryCriteria;
import jakarta.persistence.EntityManager;

/**
 * Implementacao base do {@link CommonRepository}.
 *
 * @param <T> tipo da entidade persistida
 * @param <ID> tipo do identificador da entidade
 */
public class CommonRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements CommonRepository<T, ID> {

  /**
   * Cria a implementacao base a partir dos metadados JPA da entidade.
   *
   * @param entityInformation metadados da entidade
   * @param entityManager entity manager associado
   */
  public CommonRepositoryImpl(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  /** {@inheritDoc} */
  @Override
  public PageResult<T> findAll(QueryCriteria<T> queryCriteria) {
    QueryCriteria<T> effectiveCriteria =
        queryCriteria == null ? QueryCriteria.unpaged(null) : queryCriteria;

    return PageResult.from(
        super.findAll(effectiveCriteria.specification(), effectiveCriteria.pageable()));
  }
}
