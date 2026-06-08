import { provideHttpClient, withInterceptors } from '@angular/common/http';
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

import type { SecurityFeature } from '../security.provider';
import { AUTHENTICATION_CONFIG, authenticationConfig } from './authentication.config';
import { AuthenticationService } from './authentication.service';

/**
 * Habilita autenticação via Keycloak no módulo de segurança.
 *
 * Registra:
 * - configuração de autenticação
 * - instância do Keycloak
 * - interceptor bearer para `/api/**`
 * - refresh automático de token
 * - AuthenticationService público
 */
export function withAuthentication(): SecurityFeature {
  return () => provideAuthentication();
}

function provideAuthentication(): EnvironmentProviders {
  return makeEnvironmentProviders([
    {
      provide: AUTHENTICATION_CONFIG,
      useValue: authenticationConfig,
    },

    AuthenticationService,

    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),

    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [
        createInterceptorCondition<IncludeBearerTokenCondition>({
          urlPattern: authenticationConfig.bearerTokenUrlPattern,
        }),
      ],
    },

    provideKeycloak({
      ...authenticationConfig,

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
