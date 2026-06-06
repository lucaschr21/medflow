import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import type { Observable } from 'rxjs';

import { buildFindAllParams, type FindAllParams } from '../@core/persistence/find-all.params';
import type { PageResult } from '../@core/persistence/page-result';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Organizacao, OrganizacaoInput } from '../schemas/organizacao.schema';

/**
 * Service HTTP do recurso de organizações.
 *
 * A implementação espelha o contrato REST do backend e expõe os mesmos
 * verbos usados na autorização funcional do frontend.
 */
@Injectable({ providedIn: 'root' })
export class OrganizacaoService extends ProtectedResourceService<'organizacao', true> {
  private static readonly API_URL = '/api/organizacoes';

  private readonly http = inject(HttpClient);

  protected readonly resource = 'organizacao' as const;
  protected readonly softDelete = true as const;

  /**
   * Lista organizações com filtro RSQL, paginação e ordenação.
   *
   * @param params parâmetros opcionais de listagem
   * @returns página de organizações
   */
  findAll(params: FindAllParams = {}): Observable<PageResult<Organizacao>> {
    return this.http.get<PageResult<Organizacao>>(OrganizacaoService.API_URL, {
      params: buildFindAllParams(params),
    });
  }

  /**
   * Busca uma organização pelo identificador.
   *
   * @param organizacaoId identificador da organização
   * @returns organização encontrada
   */
  findById(organizacaoId: string): Observable<Organizacao> {
    return this.http.get<Organizacao>(`${OrganizacaoService.API_URL}/${organizacaoId}`);
  }

  /**
   * Cria uma nova organização.
   *
   * @param input dados da organização
   * @returns organização persistida
   */
  create(input: OrganizacaoInput): Observable<Organizacao> {
    return this.http.post<Organizacao>(OrganizacaoService.API_URL, input);
  }

  /**
   * Atualiza uma organização existente.
   *
   * @param organizacaoId identificador da organização
   * @param input novos dados da organização
   * @returns organização atualizada
   */
  update(organizacaoId: string, input: OrganizacaoInput): Observable<Organizacao> {
    return this.http.put<Organizacao>(`${OrganizacaoService.API_URL}/${organizacaoId}`, input);
  }

  /**
   * Inativa uma organização existente.
   *
   * @param organizacaoId identificador da organização
   * @returns conclusão da operação
   */
  deactivate(organizacaoId: string): Observable<void> {
    return this.http.delete<void>(`${OrganizacaoService.API_URL}/${organizacaoId}`);
  }
}
