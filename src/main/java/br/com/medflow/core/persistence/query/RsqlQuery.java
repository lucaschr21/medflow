package br.com.medflow.core.persistence.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import io.github.perplexhub.rsql.RSQLJPASupport;

/**
 * Representa a expressao RSQL recebida em listagens filtraveis.
 *
 * <p>Este tipo concentra a conversao da query string {@code q} em
 * {@link Specification}, evitando espalhar detalhes da biblioteca de RSQL
 * pelos controllers e services.
 */
public record RsqlQuery(String expression) {

  /**
   * Indica se a expressao RSQL esta ausente ou vazia.
   *
   * @return {@code true} quando nao houver expressao valida
   */
  public boolean isEmpty() {
    return expression == null || expression.isBlank();
  }

  /**
   * Converte a expressao RSQL em uma {@link Specification}.
   *
   * @param <T> tipo da entidade alvo da consulta
   * @return especificacao gerada ou {@code null} quando a expressao estiver vazia
   */
  public <T> Specification<T> toSpecification() {
    return isEmpty() ? null : RSQLJPASupport.toSpecification(expression);
  }

  /**
   * Converte a expressao RSQL em {@link QueryCriteria} com a paginacao
   * informada.
   *
   * @param pageable paginacao e ordenacao da consulta
   * @param <T> tipo da entidade alvo da consulta
   * @return criterios prontos para uso no repository
   */
  public <T> QueryCriteria<T> toCriteria(Pageable pageable) {
    return QueryCriteria.of(toSpecification(), pageable);
  }
}
