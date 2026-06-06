import {
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
  httpResource,
  type HttpResourceRequest,
} from '@angular/common/http';
import { computed, inject, Injectable } from '@angular/core';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import Keycloak from 'keycloak-js';

import { AUTHENTICATION_CONFIG } from '../authentication/authentication.config';
import {
  type GrantedPermission,
  type PermissionDescriptor,
  type Resource,
  type Scope,
  scopes,
} from './authorization.types';

const EMPTY_PERMISSION_MAP = new Map<Resource, ReadonlySet<Scope>>();
const KEYCLOAK_READY_EVENTS = new Set<KeycloakEventType>([
  KeycloakEventType.Ready,
  KeycloakEventType.AuthSuccess,
  KeycloakEventType.AuthRefreshSuccess,
  KeycloakEventType.AuthLogout,
]);
const SCOPE_SET = new Set<Scope>(scopes);

/**
 * Store reativa das permissões funcionais carregadas do Keycloak.
 *
 * Esta store consulta o token endpoint com grant UMA e
 * `response_mode=permissions` para obter os escopos permitidos ao usuário
 * autenticado no backend.
 *
 * O resultado é normalizado em um mapa `recurso -> conjunto de escopos`,
 * consumido por guards, diretivas e services do frontend.
 */
@Injectable({ providedIn: 'root' })
export class AuthorizationStore {
  private readonly keycloak = inject(Keycloak);
  private readonly keycloakEvent = inject(KEYCLOAK_EVENT_SIGNAL);
  private readonly authenticationConfig = inject(AUTHENTICATION_CONFIG);

  private readonly permissionResource = httpResource<ReadonlyMap<Resource, ReadonlySet<Scope>>>(
    () => this.permissionRequest(),
    {
      defaultValue: EMPTY_PERMISSION_MAP,
      parse: (permissions: unknown) => this.toPermissionMap(this.asGrantedPermissions(permissions)),
    },
  );

  readonly loading = this.permissionResource.isLoading;
  readonly error = computed(() => this.toErrorMessage(this.permissionResource.error()));
  readonly initialized = computed(() => this.hasAuthorizationContext());
  readonly permissions = computed(() =>
    this.isForbidden(this.permissionResource.error())
      ? EMPTY_PERMISSION_MAP
      : this.permissionResource.value(),
  );
  readonly availableResources = computed(() => Array.from(this.permissions().keys()));

  /**
   * Indica se uma permissão funcional específica está disponível.
   *
   * @param permission descriptor no formato `recurso + escopo`
   * @returns `true` quando a permissão foi concedida ao usuário
   */
  can(permission: PermissionDescriptor): boolean {
    return this.permissions().get(permission.resource)?.has(permission.scope) ?? false;
  }

  /**
   * Verifica se todas as permissões informadas estão concedidas.
   */
  canAll(permissions: readonly PermissionDescriptor[]): boolean {
    return permissions.every((permission) => this.can(permission));
  }

  /**
   * Verifica se ao menos uma das permissões informadas está concedida.
   */
  canAny(permissions: readonly PermissionDescriptor[]): boolean {
    return permissions.some((permission) => this.can(permission));
  }

  /**
   * Força uma nova carga das permissões no `httpResource`.
   *
   * @returns `true` quando o reload foi aceito pela resource
   */
  reload(): boolean {
    return this.permissionResource.reload();
  }

  private permissionRequest(): HttpResourceRequest | undefined {
    this.keycloakEvent();

    if (!this.keycloak.authenticated || !this.keycloak.token) {
      return undefined;
    }

    return {
      url: this.tokenEndpoint(),
      method: 'POST',
      body: this.requestBody(),
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.keycloak.token}`,
        'Content-Type': 'application/x-www-form-urlencoded',
      }),
    };
  }

  private hasAuthorizationContext(): boolean {
    return (
      KEYCLOAK_READY_EVENTS.has(this.keycloakEvent().type) &&
      (!this.keycloak.authenticated || this.permissionResource.status() !== 'idle')
    );
  }

  private tokenEndpoint(): string {
    return `${this.authenticationConfig.url.replace(/\/$/, '')}/realms/${this.authenticationConfig.realm}/protocol/openid-connect/token`;
  }

  private requestBody(): string {
    return new HttpParams({
      fromObject: {
        grant_type: 'urn:ietf:params:oauth:grant-type:uma-ticket',
        audience: this.authenticationConfig.backendAudience,
        response_mode: 'permissions',
        response_include_resource_name: 'true',
      },
    }).toString();
  }

  private asGrantedPermissions(value: unknown): readonly GrantedPermission[] {
    return Array.isArray(value) ? (value as readonly GrantedPermission[]) : [];
  }

  private isForbidden(error: unknown): error is HttpErrorResponse {
    return error instanceof HttpErrorResponse && error.status === 403;
  }

  private toErrorMessage(error: unknown): string | null {
    if (error == null || this.isForbidden(error)) {
      return null;
    }

    return 'Não foi possível carregar as permissões do usuário.';
  }

  private toPermissionMap(
    permissions: readonly GrantedPermission[],
  ): ReadonlyMap<Resource, ReadonlySet<Scope>> {
    const groupedPermissions = new Map<Resource, Set<Scope>>();

    for (const permission of permissions) {
      if (!this.isResource(permission.rsname)) {
        continue;
      }

      const grantedScopes = groupedPermissions.get(permission.rsname) ?? new Set<Scope>();
      for (const scope of permission.scopes ?? []) {
        if (this.isScope(scope)) {
          grantedScopes.add(scope);
        }
      }
      groupedPermissions.set(permission.rsname, grantedScopes);
    }

    return new Map(
      Array.from(groupedPermissions.entries(), ([resource, scopes]) => [
        resource,
        new Set(scopes) as ReadonlySet<Scope>,
      ]),
    );
  }

  private isResource(value: string | undefined): value is Resource {
    return typeof value === 'string' && value.trim().length > 0;
  }

  private isScope(value: string): value is Scope {
    return SCOPE_SET.has(value as Scope);
  }
}
