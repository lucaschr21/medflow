import { HttpErrorResponse, type HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import { ErrorNotifierService } from './error-notifier.service';

/**
 * Exibe mensagens globais para falhas devolvidas pela API da aplicação.
 *
 * O interceptor atua apenas em chamadas para `/api/**`, preservando fluxos
 * internos como a resolução de permissões no Keycloak.
 */
export const httpErrorInterceptor: HttpInterceptorFn = (request, next) => {
  const notifier = inject(ErrorNotifierService);

  return next(request).pipe(
    catchError((error: unknown) => {
      if (request.url.startsWith('/api') && error instanceof HttpErrorResponse) {
        notifier.notifyHttpError(error);
      }

      return throwError(() => error);
    }),
  );
};
