export type Resource = string;

export type Scope = string;

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
  readonly rsname: string;
  readonly scopes?: string[];
}
