import { provideHttpClient, withInterceptors, type HttpInterceptorFn } from '@angular/common/http';
import { makeEnvironmentProviders, type EnvironmentProviders } from '@angular/core';
import {
  AutoRefreshTokenService,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  UserActivityService,
  createInterceptorCondition,
  includeBearerTokenInterceptor,
  provideKeycloak,
  withAutoRefreshToken,
  type IncludeBearerTokenCondition,
} from 'keycloak-angular';

import { SECURITY_CONFIG, type SecurityConfig } from '../security.config';

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
 * providers: [provideAuthentication(securityConfig)]
 * ```
 */
export function provideAuthentication(
  config: SecurityConfig,
  ...interceptors: readonly HttpInterceptorFn[]
): EnvironmentProviders {
  return makeEnvironmentProviders([
    { provide: SECURITY_CONFIG, useValue: config },
    provideHttpClient(withInterceptors([...interceptors, includeBearerTokenInterceptor])),
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
        url: config.config.url,
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
