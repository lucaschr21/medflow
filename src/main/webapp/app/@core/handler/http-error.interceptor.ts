import { HttpErrorResponse, type HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import { environment } from '../../environments/environment';
import { ErrorNotifierService } from './error-notifier.service';

const API_BASE_URL = environment.api.baseUrl.replace(/\/$/, '');

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
      if (request.url.startsWith(API_BASE_URL) && error instanceof HttpErrorResponse) {
        notifier.notifyHttpError(error);
      }

      return throwError(() => error);
    }),
  );
};
