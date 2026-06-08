import { InjectionToken } from '@angular/core';
import type { ProvideKeycloakOptions } from 'keycloak-angular';

import type { KeycloakServerConfig } from 'keycloak-js';
import { environment } from '../../../environments/environment';

export interface AuthenticationConfig extends Readonly<ProvideKeycloakOptions> {
  readonly bearerTokenUrlPattern: RegExp;
  readonly config: KeycloakServerConfig;
}

export const AUTHENTICATION_CONFIG = new InjectionToken<AuthenticationConfig>(
  'AUTHENTICATION_CONFIG',
);

export const authenticationConfig = environment.authentication satisfies AuthenticationConfig;
