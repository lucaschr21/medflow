package br.com.medflow.core.persistence.query;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

/**
 * Representa uma página da aplicação sem expor diretamente o tipo
 * {@link Page} do Spring Data para as camadas externas.
 *
 * @param <T> tipo dos itens da pagina
 */
public record PageResult<T>(
    List<T> content, int page, int size, long totalElements, int totalPages) {

  /**
   * Cria uma página imutável com cópia defensiva do conteúdo.
   *
   * @param content itens da página
   * @param page índice da página atual baseado em zero
   * @param size tamanho solicitado da página
   * @param totalElements total de registros encontrados
   * @param totalPages total de páginas calculado
   */
  public PageResult {
    content = content == null ? List.of() : List.copyOf(content);
  }

  /**
   * Converte uma {@link Page} do Spring Data em {@link PageResult}.
   *
   * @param page página de origem
   * @param <T> tipo dos itens da página
   * @return página convertida
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
   * Mapeia os itens da página preservando os metadados de paginação.
   *
   * @param mapper função de transformação do item
   * @param <R> tipo do item transformado
   * @return nova página com o conteúdo transformado
   */
  public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
    List<R> mappedContent = content.stream().<R>map(mapper).toList();
    return new PageResult<>(mappedContent, page, size, totalElements, totalPages);
  }
}
