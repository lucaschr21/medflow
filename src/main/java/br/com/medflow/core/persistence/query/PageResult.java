package br.com.medflow.core.persistence.query;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

/**
 * Representa uma pagina da aplicacao sem expor diretamente o tipo
 * {@link Page} do Spring Data para as camadas externas.
 *
 * @param <T> tipo dos itens da pagina
 */
public record PageResult<T>(
    List<T> content, int page, int size, long totalElements, int totalPages) {

  /**
   * Cria uma pagina imutavel com copia defensiva do conteudo.
   *
   * @param content itens da pagina
   * @param page indice da pagina atual baseado em zero
   * @param size tamanho solicitado da pagina
   * @param totalElements total de registros encontrados
   * @param totalPages total de paginas calculado
   */
  public PageResult {
    content = content == null ? List.of() : List.copyOf(content);
  }

  /**
   * Converte uma {@link Page} do Spring Data em {@link PageResult}.
   *
   * @param page pagina de origem
   * @param <T> tipo dos itens da pagina
   * @return pagina convertida
   */
  public static <T> PageResult<T> from(Page<T> page) {
    return new PageResult<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages());
  }

  /**
   * Mapeia os itens da pagina preservando os metadados de paginacao.
   *
   * @param mapper funcao de transformacao do item
   * @param <R> tipo do item transformado
   * @return nova pagina com o conteudo transformado
   */
  public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
    List<R> mappedContent = content.stream().<R>map(mapper).toList();
    return new PageResult<>(mappedContent, page, size, totalElements, totalPages);
  }
}
