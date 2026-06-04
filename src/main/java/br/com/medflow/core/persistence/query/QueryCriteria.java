package br.com.medflow.core.persistence.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Agrega os criterios usados por listagens da aplicacao.
 *
 * <p>Esta abstracao combina o filtro da consulta, representado por
 * {@link Specification}, com a configuracao de paginacao e ordenacao contida em
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
   * Cria criterios de consulta, normalizando pagina nao informada como
   * {@link Pageable#unpaged()}.
   *
   * @param specification filtro da consulta
   * @param pageable paginacao e ordenacao da consulta
   */
  public QueryCriteria {
    pageable = pageable == null ? Pageable.unpaged() : pageable;
  }

  /**
   * Cria criterios de consulta com filtro e paginacao.
   *
   * @param specification filtro da consulta
   * @param pageable paginacao e ordenacao da consulta
   * @param <T> tipo da entidade consultada
   * @return criterios criados
   */
  public static <T> QueryCriteria<T> of(Specification<T> specification, Pageable pageable) {
    return new QueryCriteria<>(specification, pageable);
  }

  /**
   * Cria criterios sem paginacao.
   *
   * @param specification filtro da consulta
   * @param <T> tipo da entidade consultada
   * @return criterios criados sem paginacao
   */
  public static <T> QueryCriteria<T> unpaged(Specification<T> specification) {
    return new QueryCriteria<>(specification, Pageable.unpaged());
  }

  /**
   * Combina a especificacao atual com uma nova especificacao via {@code and}.
   *
   * @param additionalSpecification especificacao adicional
   * @return novos criterios com a especificacao composta
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
