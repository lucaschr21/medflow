import { inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import {
  Router,
  type CanActivateFn,
  type CanMatchFn,
  type Route,
  type UrlSegment,
} from '@angular/router';
import { createAuthGuard } from 'keycloak-angular';
import { filter, firstValueFrom, map } from 'rxjs';

import { buildRedirectTree } from '../guard.utils';
import { AuthenticationService } from './authentication.service';

/**
 * Opções de comportamento do guard de autenticação.
 */
export interface AuthenticationGuardOptions {
  readonly loginOnDeny?: boolean;
  readonly redirectTo?: string;
}

/**
 * Protege uma rota que exige usuário autenticado.
 *
 * Quando o usuário não estiver autenticado, o guard pode:
 * - iniciar login no Keycloak com `loginOnDeny`
 * - redirecionar para uma rota local com `redirectTo`
 *
 * @example
 * ```ts
 * {
 *   path: 'agenda',
 *   canActivate: [authenticationGuard({ redirectTo: '/' })],
 * }
 * ```
 */
export function authenticationGuard(options: AuthenticationGuardOptions = {}): CanActivateFn {
  return createAuthGuard<CanActivateFn>(async (_route, _state, authData) => {
    if (authData.authenticated) {
      return true;
    }

    const authenticationService = inject(AuthenticationService);
    if (options.loginOnDeny) {
      await authenticationService.login();
      return false;
    }

    return buildRedirectTree(inject(Router), options.redirectTo ?? '/');
  });
}

/**
 * Variante de {@link authenticationGuard} para {@code canMatch}.
 */
export function authenticationMatchGuard(options: AuthenticationGuardOptions = {}): CanMatchFn {
  return async (_route: Route, _segments: UrlSegment[]) => {
    const authenticationService = inject(AuthenticationService);
    const router = inject(Router);

    const authenticated = await firstValueFrom(
      toObservable(authenticationService.ready).pipe(
        filter(Boolean),
        map(() => authenticationService.authenticated()),
      ),
    );

    if (authenticated) {
      return true;
    }

    if (options.loginOnDeny) {
      await authenticationService.login();
      return false;
    }

    return buildRedirectTree(router, options.redirectTo ?? '/');
  };
}
