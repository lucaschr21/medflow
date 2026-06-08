import { InjectionToken } from '@angular/core';

import { environment } from '../../../environments/environment';

export interface AuthorizationConfig {
  readonly resourceId: string;
}

export const AUTHORIZATION_CONFIG = new InjectionToken<AuthorizationConfig>('AUTHORIZATION_CONFIG');

export const authorizationConfig = {
  resourceId: environment.authorization.resourceId,
} as const satisfies AuthorizationConfig;
