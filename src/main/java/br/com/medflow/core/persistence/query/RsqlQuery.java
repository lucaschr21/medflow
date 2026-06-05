package br.com.medflow.core.persistence.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import io.github.perplexhub.rsql.RSQLJPASupport;

/**
 * Representa a expressão RSQL recebida em listagens filtráveis.
 *
 * <p>Este tipo concentra a conversão da query string {@code q} em
 * {@link Specification}, evitando espalhar detalhes da biblioteca de RSQL
 * pelos controllers e services.
 */
public record RsqlQuery(String expression) {

  /**
   * Indica se a expressão RSQL está ausente ou vazia.
   *
   * @return {@code true} quando não houver expressão válida
   */
  public boolean isEmpty() {
    return expression == null || expression.isBlank();
  }

  /**
   * Converte a expressão RSQL em uma {@link Specification}.
   *
   * @param <T> tipo da entidade alvo da consulta
   * @return especificação gerada ou {@code null} quando a expressão estiver
   *         vazia
   */
  public <T> Specification<T> toSpecification() {
    return isEmpty() ? null : RSQLJPASupport.toSpecification(expression);
  }

  /**
   * Converte a expressão RSQL em {@link QueryCriteria} com a paginação
   * informada.
   *
   * @param pageable paginação e ordenação da consulta
   * @param <T> tipo da entidade alvo da consulta
   * @return critérios prontos para uso no repository
   */
  public <T> QueryCriteria<T> toCriteria(Pageable pageable) {
    return QueryCriteria.of(toSpecification(), pageable);
  }
}
