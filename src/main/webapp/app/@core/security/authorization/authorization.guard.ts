import { inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { type CanActivateFn, type CanMatchFn, Router } from '@angular/router';
import { combineLatest, filter, firstValueFrom, map } from 'rxjs';

import { type AuthenticationGuardOptions } from '../authentication/authentication.guard';
import { AuthenticationService } from '../authentication/authentication.service';
import { buildRedirectTree } from '../guard.utils';
import { AuthorizationService } from './authorization.service';
import type { PermissionDescriptor } from './authorization.types';

/**
 * Opções de comportamento do guard de autorização.
 */
export interface AuthorizationGuardOptions extends AuthenticationGuardOptions {
  readonly mode?: 'all' | 'any';
  readonly unauthorizedRedirectTo?: string;
}

/**
 * Protege uma rota por permissão funcional.
 *
 * O guard espera o estado de autenticação e a carga inicial de permissões
 * antes de decidir o acesso.
 *
 * @example
 * ```ts
 * {
 *   path: 'organizacoes',
 *   canActivate: [
 *     authorizationGuard(
 *       { resource: 'organizacao', scope: 'read' },
 *       { unauthorizedRedirectTo: '/' },
 *     ),
 *   ],
 * }
 * ```
 */
export function authorizationGuard(
  permissions: PermissionDescriptor | readonly PermissionDescriptor[],
  options: AuthorizationGuardOptions = {},
): CanActivateFn {
  const normalizedPermissions = normalizePermissions(permissions);

  return async () => resolveAuthorization(normalizedPermissions, options);
}

/**
 * Variante de {@link authorizationGuard} para {@code canMatch}.
 */
export function authorizationMatchGuard(
  permissions: PermissionDescriptor | readonly PermissionDescriptor[],
  options: AuthorizationGuardOptions = {},
): CanMatchFn {
  const normalizedPermissions = normalizePermissions(permissions);

  return async () => resolveAuthorization(normalizedPermissions, options);
}

async function resolveAuthorization(
  permissions: readonly PermissionDescriptor[],
  options: AuthorizationGuardOptions,
) {
  const authenticationService = inject(AuthenticationService);
  const authorizationService = inject(AuthorizationService);
  const router = inject(Router);

  const [authenticated] = await firstValueFrom(
    combineLatest([
      toObservable(authenticationService.ready),
      toObservable(authenticationService.authenticated),
      toObservable(authorizationService.initialized),
    ]).pipe(
      filter(([ready, authenticated, initialized]) => ready && (!authenticated || initialized)),
      map(([, authenticated]) => [authenticated] as const),
    ),
  );

  if (!authenticated) {
    if (options.loginOnDeny) {
      await authenticationService.login();
      return false;
    }

    return buildRedirectTree(router, options.redirectTo ?? '/');
  }

  return isAuthorized(authorizationService, permissions, options.mode)
    ? true
    : buildRedirectTree(router, options.unauthorizedRedirectTo ?? options.redirectTo ?? '/');
}

function isAuthorized(
  authorizationService: AuthorizationService,
  permissions: readonly PermissionDescriptor[],
  mode: AuthorizationGuardOptions['mode'],
): boolean {
  return mode === 'any'
    ? authorizationService.canAny(permissions)
    : authorizationService.canAll(permissions);
}

function normalizePermissions(
  permissions: PermissionDescriptor | readonly PermissionDescriptor[],
): readonly PermissionDescriptor[] {
  return Array.isArray(permissions) ? permissions : [permissions as PermissionDescriptor];
}
