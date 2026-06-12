import { inject } from '@angular/core';
import type { Observable } from 'rxjs';
import { of } from 'rxjs';

import type { FindAllParams } from '../../persistence/find-all.params';
import type { PageResult } from '../../persistence/page-result';
import { DemoMedflowDataService } from '../../mock/demo-medflow-data.service';
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

  private readonly authorizationService = inject(AuthorizationService);
  private readonly demoData = inject(DemoMedflowDataService);

  /**
   * Lista registros do recurso com filtro, paginação e ordenação.
   *
   * @param params parâmetros opcionais de listagem
   * @returns página do recurso
   */
  findAll(params: FindAllParams = {}): Observable<PageResult<Entity>> {
    this.ensureAllowed('read');
    return of(this.demoData.list(this.resource, params) as PageResult<Entity>);
  }

  /**
   * Busca um registro pelo identificador.
   *
   * @param id identificador do recurso
   * @returns registro encontrado
   */
  findById(id: string): Observable<Entity> {
    this.ensureAllowed('read');
    return of(this.demoData.findById(this.resource, id) as Entity);
  }

  /**
   * Cria um novo registro.
   *
   * @param input payload de entrada
   * @returns recurso persistido
   */
  create(input: Input): Observable<Entity> {
    this.ensureAllowed('create');
    return of(this.demoData.create(this.resource, input) as Entity);
  }

  /**
   * Atualiza um registro existente.
   *
   * @param id identificador do recurso
   * @param input payload de entrada
   * @returns recurso atualizado
   */
  update(id: string, input: Input): Observable<Entity> {
    this.ensureAllowed('update');
    return of(this.demoData.update(this.resource, id, input) as Entity);
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
    this.ensureAllowed(this.removeScope);
    this.demoData.remove(this.resource, id);
    return of(void 0);
  }

  private permission<ScopeType extends Scope>(
    scope: ScopeType,
  ): PermissionTuple<ResourceType, ScopeType> {
    return [this.resource, scope];
  }

  private ensureAllowed(scope: Scope): void {
    this.authorizationService.ensureAllowed(this.permission(scope));
  }
}
