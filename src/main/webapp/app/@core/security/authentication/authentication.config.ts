import { InjectionToken } from '@angular/core';
import type { KeycloakInitOptions } from 'keycloak-js';

import { environment } from '../../../environments/environment';

type AuthenticationInitOptions = Omit<KeycloakInitOptions, 'silentCheckSsoRedirectUri'>;

/**
 * Configuração base de autenticação do frontend.
 *
 * O core usa esse contrato para:
 * - inicializar o Keycloak
 * - configurar o client SPA
 * - conhecer a audience do backend usada no fluxo UMA
 */
export interface AuthenticationConfig {
  readonly url: string;
  readonly realm: string;
  readonly clientId: string;
  readonly backendAudience: string;
  readonly initOptions: AuthenticationInitOptions;
}

/**
 * Injection token da configuração de autenticação do frontend.
 */
export const AUTHENTICATION_CONFIG = new InjectionToken<AuthenticationConfig>(
  'AUTHENTICATION_CONFIG',
);

/**
 * Configuração padrão de autenticação derivada do environment atual.
 *
 * O projeto inicia a sessão com `check-sso`, permitindo que a SPA descubra
 * uma sessão já existente sem forçar login na carga inicial.
 */
export const authenticationConfig: AuthenticationConfig = {
  ...environment.authentication,
  initOptions: {
    onLoad: 'check-sso',
    checkLoginIframe: false,
    redirectUri: location.origin,
  },
};
