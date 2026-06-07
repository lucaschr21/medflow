import { InjectionToken } from '@angular/core';
import type { ProvideKeycloakOptions } from 'keycloak-angular';
import type { KeycloakServerConfig } from 'keycloak-js';

import { environment } from '../../environments/environment';

export interface SecurityServerConfig extends Readonly<KeycloakServerConfig> {
  readonly resourceId: string;
}

export interface SecurityConfig extends Omit<ProvideKeycloakOptions, 'config'> {
  readonly config: SecurityServerConfig;
}

export const SECURITY_CONFIG = new InjectionToken<SecurityConfig>('SECURITY_CONFIG');

export const securityConfig = {
  config: environment.config,
  initOptions: environment.initOptions,
} as const satisfies SecurityConfig;
