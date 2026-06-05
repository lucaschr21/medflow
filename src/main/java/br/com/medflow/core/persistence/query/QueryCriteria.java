package br.com.medflow.core.persistence.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Agrega os critérios usados por listagens da aplicação.
 *
 * <p>Esta abstração combina o filtro da consulta, representado por
 * {@link Specification}, com a configuração de paginação e ordenação contida em
 * {@link Pageable}.
 *
 * <p>Uso tipico:
 *
 * <pre>{@code
 * QueryCriteria<Usuario> criteria =
 *     rsqlQuery.toCriteria(pageable)
 *         .and(UsuarioSpecifications.ativos());
 * }</pre>
 *
 * @param <T> tipo da entidade consultada
 */
public record QueryCriteria<T>(Specification<T> specification, Pageable pageable) {

  /**
   * Cria critérios de consulta, normalizando página não informada como
   * {@link Pageable#unpaged()}.
   *
   * @param specification filtro da consulta
   * @param pageable paginação e ordenação da consulta
   */
  public QueryCriteria {
    pageable = pageable == null ? Pageable.unpaged() : pageable;
  }

  /**
   * Cria critérios de consulta com filtro e paginação.
   *
   * @param specification filtro da consulta
   * @param pageable paginação e ordenação da consulta
   * @param <T> tipo da entidade consultada
   * @return critérios criados
   */
  public static <T> QueryCriteria<T> of(Specification<T> specification, Pageable pageable) {
    return new QueryCriteria<>(specification, pageable);
  }

  /**
   * Cria critérios sem paginação.
   *
   * @param specification filtro da consulta
   * @param <T> tipo da entidade consultada
   * @return critérios criados sem paginação
   */
  public static <T> QueryCriteria<T> unpaged(Specification<T> specification) {
    return new QueryCriteria<>(specification, Pageable.unpaged());
  }

  /**
   * Combina a especificação atual com uma nova especificação via {@code and}.
   *
   * @param additionalSpecification especificação adicional
   * @return novos critérios com a especificação composta
   */
  public QueryCriteria<T> and(Specification<T> additionalSpecification) {
    if (additionalSpecification == null) {
      return this;
    }

    if (specification == null) {
      return new QueryCriteria<>(additionalSpecification, pageable);
    }

    return new QueryCriteria<>(specification.and(additionalSpecification), pageable);
  }
}
