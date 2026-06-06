import { makeEnvironmentProviders, type EnvironmentProviders } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import {
  AutoRefreshTokenService,
  createInterceptorCondition,
  type IncludeBearerTokenCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  UserActivityService,
  includeBearerTokenInterceptor,
  provideKeycloak,
  withAutoRefreshToken,
} from 'keycloak-angular';

import { AUTHENTICATION_CONFIG, type AuthenticationConfig } from './authentication.config';

/**
 * Registra a infraestrutura de autenticação do frontend.
 *
 * Este provider agrupa:
 * - o bootstrap do Keycloak
 * - o interceptor bearer para chamadas `/api/**`
 * - o refresh automático de token por atividade do usuário
 *
 * @param config configuração de autenticação do ambiente atual
 * @returns providers de ambiente para o bootstrap da aplicação
 *
 * @example
 * ```ts
 * providers: [provideAuthentication(authenticationConfig)]
 * ```
 */
export function provideAuthentication(config: AuthenticationConfig): EnvironmentProviders {
  return makeEnvironmentProviders([
    { provide: AUTHENTICATION_CONFIG, useValue: config },
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),
    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [
        createInterceptorCondition<IncludeBearerTokenCondition>({
          urlPattern: /^\/api(?:\/.*)?$/i,
        }),
      ],
    },
    provideKeycloak({
      config: {
        url: new URL(config.url).toString().replace(/\/$/, ''),
        realm: config.realm,
        clientId: config.clientId,
      },
      initOptions: config.initOptions,
      features: [
        withAutoRefreshToken({
          onInactivityTimeout: 'logout',
          sessionTimeout: 5 * 60_000,
        }),
      ],
      providers: [AutoRefreshTokenService, UserActivityService],
    }),
  ]);
}
