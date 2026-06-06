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
  const fromObject: Record<string, string | number | readonly string[]> = {};

  if (params.q) {
    fromObject['q'] = params.q;
  }

  if (params.page != null) {
    fromObject['page'] = params.page;
  }

  if (params.size != null) {
    fromObject['size'] = params.size;
  }

  if (params.sort) {
    fromObject['sort'] = Array.isArray(params.sort) ? params.sort : [params.sort];
  }

  return new HttpParams({
    fromObject,
  });
}
