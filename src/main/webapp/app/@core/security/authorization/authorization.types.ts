export type Resource = string;

export const scopes = ['read', 'create', 'update', 'delete', 'deactivate'] as const;

export type Scope = (typeof scopes)[number];

/**
 * Forma padronizada de declarar uma permissão funcional.
 */
export type PermissionTuple<
  ResourceType extends Resource = Resource,
  ScopeType extends Scope = Scope,
> = readonly [resource: ResourceType, scope: ScopeType];

/**
 * Estrutura simplificada retornada pelo Keycloak Authorization Services em
 * `response_mode=permissions`.
 */
export interface GrantedPermission {
  readonly rsid: string;
  readonly rsname?: string;
  readonly scopes?: string[];
}
