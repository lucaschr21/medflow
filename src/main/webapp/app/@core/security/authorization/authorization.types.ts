export type Resource = string;

export const scopes = ['read', 'create', 'update', 'delete', 'deactivate'] as const;

export type Scope = (typeof scopes)[number];

/**
 * Subconjunto de métodos HTTP suportados pela inferência de escopo do
 * frontend.
 *
 * O mapeamento segue a mesma convenção do backend:
 * - `GET`/`HEAD`/`OPTIONS` -> `read`
 * - `POST` -> `create`
 * - `PUT`/`PATCH` -> `update`
 * - `DELETE` -> `delete` ou `deactivate`
 */
export type AuthorizedHttpMethod = 'GET' | 'HEAD' | 'OPTIONS' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

interface HttpMethodScopeMap {
  GET: 'read';
  HEAD: 'read';
  OPTIONS: 'read';
  POST: 'create';
  PUT: 'update';
  PATCH: 'update';
}

export type ScopeFromHttpMethod<
  Method extends AuthorizedHttpMethod,
  SoftDelete extends boolean,
> = Method extends keyof HttpMethodScopeMap
  ? HttpMethodScopeMap[Method]
  : Method extends 'DELETE'
    ? SoftDelete extends true
      ? 'deactivate'
      : 'delete'
    : never;

export interface PermissionDescriptor<
  ResourceType extends Resource = Resource,
  ScopeType extends Scope = Scope,
> {
  /**
   * Nome funcional do recurso protegido.
   */
  readonly resource: ResourceType;
  /**
   * Escopo funcional exigido para a operação.
   */
  readonly scope: ScopeType;
}

/**
 * Forma curta de declarar uma permissão funcional em templates.
 */
export type PermissionTuple<
  ResourceType extends Resource = Resource,
  ScopeType extends Scope = Scope,
> = readonly [resource: ResourceType, scope: ScopeType];

/**
 * Formas aceitas para declarar uma permissão funcional na UI.
 *
 * Regras:
 * - `resource` isolado significa `resource:read`
 * - `[resource, scope]` representa `resource:scope`
 * - `PermissionDescriptor` representa a forma explícita completa
 */
export type PermissionInput<
  ResourceType extends Resource = Resource,
  ScopeType extends Scope = Scope,
> =
  | ResourceType
  | PermissionTuple<ResourceType, ScopeType>
  | PermissionDescriptor<ResourceType, ScopeType>;

/**
 * Estrutura simplificada retornada pelo Keycloak Authorization Services em
 * `response_mode=permissions`.
 */
export interface GrantedPermission {
  readonly rsid: string;
  readonly rsname?: string;
  readonly scopes?: string[];
}
