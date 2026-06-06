import type { ApplicationConfig } from '@angular/core';
import { provideBrowserGlobalErrorListeners } from '@angular/core';
import { inject } from '@angular/core';
import { provideRouter, withNavigationErrorHandler } from '@angular/router';
import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';
import { providePrimeNG } from 'primeng/config';

import { ErrorNotifierService } from './@core/handler/error-notifier.service';
import { httpErrorInterceptor } from './@core/handler/http-error.interceptor';
import { provideHandler } from './@core/handler/handler.providers';
import { authenticationConfig } from './@core/security/authentication/authentication.config';
import { provideAuthentication } from './@core/security/authentication/authentication.providers';
import { routes } from './app.routes';

const MedflowPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{violet.50}',
      100: '{violet.100}',
      200: '{violet.200}',
      300: '{violet.300}',
      400: '{violet.400}',
      500: '{violet.500}',
      600: '{violet.600}',
      700: '{violet.700}',
      800: '{violet.800}',
      900: '{violet.900}',
      950: '{violet.950}',
    },
  },
});

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
          darkModeSelector: '.app-dark',
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
