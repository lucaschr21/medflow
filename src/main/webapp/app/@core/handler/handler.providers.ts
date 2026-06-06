import { ErrorHandler, makeEnvironmentProviders, type EnvironmentProviders } from '@angular/core';
import { MessageService } from 'primeng/api';

import { GlobalErrorHandler } from './global-error.handler';

/**
 * Registra a infraestrutura global de tratamento de erros do frontend.
 *
 * O provider publica:
 * - `MessageService` do PrimeNG para toasts globais
 * - o `ErrorHandler` raiz da aplicação
 *
 * @returns providers de ambiente do módulo de handler
 */
export function provideHandler(): EnvironmentProviders {
  return makeEnvironmentProviders([
    MessageService,
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
  ]);
}
