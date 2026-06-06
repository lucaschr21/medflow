/**
 * Página retornada pelo backend para listagens paginadas.
 *
 * O formato espelha o `PageResult` do backend para evitar adaptação extra no
 * frontend.
 *
 * @typeParam T tipo dos itens da página
 */
export interface PageResult<T> {
  readonly content: readonly T[];
  readonly page: number;
  readonly size: number;
  readonly totalElements: number;
  readonly totalPages: number;
}
