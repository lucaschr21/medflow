import { Injectable, inject, type ErrorHandler } from '@angular/core';

import { ErrorNotifierService } from './error-notifier.service';

/**
 * Trata falhas inesperadas encaminhadas pelo Angular ao `ErrorHandler` raiz.
 *
 * Esse handler não substitui o tratamento local dos fluxos da aplicação. Ele
 * atua apenas como último fallback para exibir uma mensagem e manter o erro no
 * console durante o desenvolvimento.
 */
@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  private readonly notifier = inject(ErrorNotifierService);

  handleError(error: unknown): void {
    console.error(error);
    this.notifier.notifyUnexpected(this.unwrap(error));
  }

  private unwrap(error: unknown): unknown {
    return typeof error === 'object' && error !== null && 'rejection' in error
      ? (error as { rejection: unknown }).rejection
      : error;
  }
}
