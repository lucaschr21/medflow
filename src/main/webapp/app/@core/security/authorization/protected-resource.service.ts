import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import type { Observable } from 'rxjs';

import { buildFindAllParams, type FindAllParams } from '../../persistence/find-all.params';
import type { PageResult } from '../../persistence/page-result';
import { environment } from '../../../environments/environment';
import { AuthorizationService } from './authorization.service';
import type { PermissionTuple, Resource, Scope } from './authorization.types';

/**
 * Classe base para services de recurso protegido do frontend.
 *
 * Cada service concreto informa apenas o path REST do recurso.
 *
 * @example
 * ```ts
 * export class OrganizacaoService
 *   extends ProtectedResourceService<'organizacao', 'deactivate', Organizacao, OrganizacaoInput> {
 *   protected readonly resource = 'organizacao' as const;
 *   protected readonly resourcePath = 'organizacoes';
 *   protected readonly removeScope = 'deactivate' as const;
 * }
 * ```
 */
export abstract class ProtectedResourceService<
  ResourceType extends Resource,
  RemoveScope extends Extract<Scope, 'delete' | 'deactivate'>,
  Entity,
  Input,
> {
  protected abstract readonly resource: ResourceType;
  protected abstract readonly resourcePath: string;
  protected abstract readonly removeScope: RemoveScope;

  protected readonly http = inject(HttpClient);
  private readonly apiBaseUrl = environment.api.baseUrl;
  private readonly authorizationService = inject(AuthorizationService);

  /**
   * Lista registros do recurso com filtro, paginação e ordenação.
   *
   * @param params parâmetros opcionais de listagem
   * @returns página do recurso
   */
  findAll(params: FindAllParams = {}): Observable<PageResult<Entity>> {
    return this.http.get<PageResult<Entity>>(this.resourceUrl, {
      params: buildFindAllParams(params),
    });
  }

  /**
   * Busca um registro pelo identificador.
   *
   * @param id identificador do recurso
   * @returns registro encontrado
   */
  findById(id: string): Observable<Entity> {
    return this.http.get<Entity>(this.resourceItemUrl(id));
  }

  /**
   * Cria um novo registro.
   *
   * @param input payload de entrada
   * @returns recurso persistido
   */
  create(input: Input): Observable<Entity> {
    return this.http.post<Entity>(this.resourceUrl, input);
  }

  /**
   * Atualiza um registro existente.
   *
   * @param id identificador do recurso
   * @param input payload de entrada
   * @returns recurso atualizado
   */
  update(id: string, input: Input): Observable<Entity> {
    return this.http.put<Entity>(this.resourceItemUrl(id), input);
  }

  /**
   * Remove um registro usando `DELETE`.
   *
   * Para recursos com soft delete, a inativação continua sendo resolvida pelo
   * backend de forma transparente ao frontend.
   *
   * @param id identificador do recurso
   * @returns conclusão da operação
   */
  remove(id: string): Observable<void> {
    return this.http.delete<void>(this.resourceItemUrl(id));
  }

  protected get resourceUrl(): string {
    return `${this.apiBaseUrl}/${this.resourcePath}`;
  }

  private resourceItemUrl(id: string): string {
    return `${this.resourceUrl}/${id}`;
  }

  private permission<ScopeType extends Scope>(
    scope: ScopeType,
  ): PermissionTuple<ResourceType, ScopeType> {
    return [this.resource, scope];
  }
}
