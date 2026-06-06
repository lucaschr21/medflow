import {
  type AuthorizedHttpMethod,
  type PermissionDescriptor,
  type Resource,
  type ScopeFromHttpMethod,
} from './authorization.types';

/**
 * Classe base para services de recurso protegido do frontend.
 *
 * Cada service concreto informa:
 * - o nome funcional do recurso
 * - se `DELETE` significa `deactivate` ou `delete`
 *
 * @example
 * ```ts
 * export class OrganizacaoService
 *   extends ProtectedResourceService<'organizacao', true> {
 *   protected readonly resource = 'organizacao' as const;
 *   protected readonly softDelete = true;
 * }
 * ```
 */
export abstract class ProtectedResourceService<
  ResourceType extends Resource,
  SoftDelete extends boolean,
> {
  protected abstract readonly resource: ResourceType;
  protected abstract readonly softDelete: SoftDelete;

  readonly permissions = {
    read: this.permission('GET'),
    create: this.permission('POST'),
    update: this.permission('PUT'),
    remove: this.permission('DELETE'),
  } as const;

  /**
   * Cria um descriptor de permissão a partir do método HTTP da operação.
   *
   * @param method método HTTP usado pela operação protegida
   * @returns descriptor compatível com guards e diretivas do core
   */
  protected permission<Method extends AuthorizedHttpMethod>(
    method: Method,
  ): PermissionDescriptor<ResourceType, ScopeFromHttpMethod<Method, SoftDelete>> {
    return {
      resource: this.resource,
      scope: this.scopeFromHttpMethod(method),
    };
  }

  private scopeFromHttpMethod<Method extends AuthorizedHttpMethod>(
    method: Method,
  ): ScopeFromHttpMethod<Method, SoftDelete> {
    switch (method) {
      case 'GET':
      case 'HEAD':
      case 'OPTIONS':
        return 'read' as ScopeFromHttpMethod<Method, SoftDelete>;
      case 'POST':
        return 'create' as ScopeFromHttpMethod<Method, SoftDelete>;
      case 'PUT':
      case 'PATCH':
        return 'update' as ScopeFromHttpMethod<Method, SoftDelete>;
      case 'DELETE':
        return (this.softDelete ? 'deactivate' : 'delete') as ScopeFromHttpMethod<
          Method,
          SoftDelete
        >;
      default:
        return assertNever(method);
    }
  }
}

function assertNever(value: never): never {
  throw new Error(`Unsupported HTTP method: ${value}`);
}
