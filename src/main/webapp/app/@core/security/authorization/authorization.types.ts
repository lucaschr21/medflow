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
 * `response_mode=permissions` com `response_include_resource_name=true`.
 */
export interface GrantedPermission {
  readonly rsid: string;
  readonly rsname: Resource;
  readonly scopes?: readonly Scope[];
}
