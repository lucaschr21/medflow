import { makeEnvironmentProviders } from '@angular/core';
import type { SecurityFeature } from '../security.provider';
import type { AuthorizationConfig } from './authorization.config';
import { AUTHORIZATION_CONFIG, authorizationConfig } from './authorization.config';
import { AuthorizationService } from './authorization.service';

export function withAuthorization(
  config: AuthorizationConfig = authorizationConfig,
): SecurityFeature {
  return () =>
    makeEnvironmentProviders([
      {
        provide: AUTHORIZATION_CONFIG,
        useValue: config,
      },

      AuthorizationService,
    ]);
}
