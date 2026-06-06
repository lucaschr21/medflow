import type { ApplicationConfig } from '@angular/core';
import { provideBrowserGlobalErrorListeners } from '@angular/core';
import { inject } from '@angular/core';
import { provideRouter, withNavigationErrorHandler } from '@angular/router';
import { MedflowPreset } from './@shared/theme/medflow.preset';
import { providePrimeNG } from 'primeng/config';

import { ErrorNotifierService } from './@core/handler/error-notifier.service';
import { httpErrorInterceptor } from './@core/handler/http-error.interceptor';
import { provideHandler } from './@core/handler/handler.providers';
import { authenticationConfig } from './@core/security/authentication/authentication.config';
import { provideAuthentication } from './@core/security/authentication/authentication.providers';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideAuthentication(authenticationConfig, httpErrorInterceptor),
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
