import { HttpParams } from '@angular/common/http';

/**
 * Parâmetros compartilhados pelas listagens paginadas do backend.
 *
 * O contrato acompanha o padrão usado nos endpoints do Medflow:
 * - `q` para filtro RSQL
 * - `page` e `size` para paginação
 * - `sort` para ordenação
 */
export interface FindAllParams {
  readonly q?: string;
  readonly page?: number;
  readonly size?: number;
  readonly sort?: string | readonly string[];
}

/**
 * Converte os parâmetros de listagem em `HttpParams`.
 *
 * O helper evita repetição nos services e preserva a semântica do backend para
 * ordenação múltipla com parâmetros `sort` repetidos.
 *
 * @param params parâmetros opcionais de filtro, paginação e ordenação
 * @returns instância de `HttpParams` pronta para uso no `HttpClient`
 *
 * @example
 * ```ts
 * this.http.get<PageResult<Usuario>>(url, {
 *   params: buildFindAllParams({
 *     q: 'nome==joao*',
 *     page: 0,
 *     size: 20,
 *     sort: ['nome,asc', 'createdAt,desc'],
 *   }),
 * });
 * ```
 */
export function buildFindAllParams(params: FindAllParams = {}): HttpParams {
  return new HttpParams({
    fromObject: {
      ...(params.q ? { q: params.q } : {}),
      ...(params.page != null ? { page: params.page } : {}),
      ...(params.size != null ? { size: params.size } : {}),
      ...(params.sort ? { sort: Array.isArray(params.sort) ? params.sort : [params.sort] } : {}),
    },
  });
}
