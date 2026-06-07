import type { ApplicationConfig } from '@angular/core';
import { inject, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withNavigationErrorHandler } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import { MedflowPreset } from './@shared/theme/medflow.preset';

import { ErrorNotifierService } from './@core/handler/error-notifier.service';
import { provideHandler } from './@core/handler/handler.providers';
import { httpErrorInterceptor } from './@core/handler/http-error.interceptor';
import { provideAuthentication } from './@core/security/authentication/authentication.providers';
import { securityConfig } from './@core/security/security.config';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideAuthentication(securityConfig, httpErrorInterceptor),
    provideHandler(),
    providePrimeNG({
      ripple: true,
      theme: {
        preset: MedflowPreset,
        options: {
          darkModeSelector: false,
        },
      },
    }),
    provideRouter(
      routes,
      withNavigationErrorHandler((error) => {
        inject(ErrorNotifierService).notifyUnexpected(error);
      }),
    ),
  ],
};
