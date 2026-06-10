import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { computed, inject, Injectable } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { catchError, map, of, throwError, type Observable } from 'rxjs';

import { AUTHENTICATION_CONFIG } from '../authentication/authentication.config';
import { AuthenticationService } from '../authentication/authentication.service';
import { AUTHORIZATION_CONFIG } from './authorization.config';
import {
  type GrantedPermission,
  type PermissionTuple,
  type Resource,
  type Scope,
} from './authorization.types';

type PermissionMap = ReadonlyMap<Resource, ReadonlySet<Scope>>;

const UMA_GRANT_TYPE = 'urn:ietf:params:oauth:grant-type:uma-ticket';

@Injectable()
export class AuthorizationService {
  private readonly http = inject(HttpClient);
  private readonly authentication = inject(AuthenticationService);
  private readonly authenticationConfig = inject(AUTHENTICATION_CONFIG);
  private readonly authorizationConfig = inject(AUTHORIZATION_CONFIG);

  private readonly tokenEndpointUrl = `${this.authenticationConfig.config.url}/realms/${this.authenticationConfig.config.realm}/protocol/openid-connect/token`;

  private readonly permissionRequestBody = new HttpParams({
    fromObject: {
      grant_type: UMA_GRANT_TYPE,
      audience: this.authorizationConfig.resourceId,
      response_mode: 'permissions',
      response_include_resource_name: 'true',
    },
  });

  private readonly permissionResource = rxResource<PermissionMap, string | undefined>({
    defaultValue: EMPTY_PERMISSION_MAP,

    params: () => this.authentication.token() ?? undefined,

    stream: ({ params: token }) =>
      token === undefined ? of(EMPTY_PERMISSION_MAP) : this.loadPermissions(),
  });

  readonly loading = this.permissionResource.isLoading;

  readonly error = this.permissionResource.error;

  readonly ready = computed(
    () =>
      this.authentication.ready() &&
      (this.authentication.token() == null || this.permissionResource.status() !== 'idle'),
  );

  readonly permissions = computed<PermissionMap>(() =>
    this.error() == null ? this.permissionResource.value() : EMPTY_PERMISSION_MAP,
  );

  readonly resources = computed<readonly Resource[]>(() => Array.from(this.permissions().keys()));

  can([resource, scope]: PermissionTuple): boolean {
    return this.permissions().get(resource)?.has(scope) ?? false;
  }

  canAll(permissions: readonly PermissionTuple[]): boolean {
    return permissions.every((permission) => this.can(permission));
  }

  canAny(permissions: readonly PermissionTuple[]): boolean {
    return permissions.some((permission) => this.can(permission));
  }

  reload(): boolean {
    return this.permissionResource.reload();
  }

  private loadPermissions(): Observable<PermissionMap> {
    return this.http
      .post<readonly GrantedPermission[]>(this.tokenEndpointUrl, this.permissionRequestBody)
      .pipe(
        map((permissions) => this.toPermissionMap(permissions)),
        catchError((error: unknown) =>
          error instanceof HttpErrorResponse && error.status === 403
            ? of(EMPTY_PERMISSION_MAP)
            : throwError(() => error),
        ),
      );
  }

  private toPermissionMap(permissions: readonly GrantedPermission[]): PermissionMap {
    return new Map(permissions.map(({ rsname, scopes = [] }) => [rsname, new Set(scopes)]));
  }
}

const EMPTY_PERMISSION_MAP: PermissionMap = new Map<Resource, ReadonlySet<Scope>>();
