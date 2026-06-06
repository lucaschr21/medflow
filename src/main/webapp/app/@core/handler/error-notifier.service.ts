import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService, type ToastMessageOptions } from 'primeng/api';

import { AuthorizationError } from '../security/authorization/authorization.error';
import type { ProblemDetail } from './problem-detail';

const HANDLED_ERROR = Symbol('handled-error');
const DEFAULT_ERROR_DETAIL = 'Ocorreu um erro inesperado ao processar a solicitação.';
const DEFAULT_ERROR_SUMMARY = 'Erro inesperado';
const ACCESS_DENIED_DETAIL = 'Você não possui permissão para executar esta ação.';
const PERMISSION_ERROR_SUMMARY = 'Permissões indisponíveis';
const PERMISSION_ERROR_DETAIL =
  'Não foi possível carregar suas permissões agora. Seu acesso pode aparecer limitado.';
const VALIDATION_STATUS = 422;

/**
 * Centraliza a conversão de erros técnicos em mensagens visuais da aplicação.
 *
 * O serviço conversa com o `MessageService` do PrimeNG e entende o contrato de
 * erro HTTP do backend para exibir mensagens mais amigáveis ao usuário.
 */
@Injectable({ providedIn: 'root' })
export class ErrorNotifierService {
  private readonly messageService = inject(MessageService);

  /**
   * Exibe uma mensagem para falhas retornadas pela API.
   *
   * @param error erro HTTP recebido pelo Angular
   */
  notifyHttpError(error: HttpErrorResponse): void {
    if (this.wasHandled(error)) {
      return;
    }

    this.messageService.add(this.toHttpToast(error));
    this.markHandled(error);
  }

  /**
   * Exibe uma mensagem para falhas inesperadas não tratadas pela aplicação.
   *
   * @param error erro recebido pelo `ErrorHandler`
   */
  notifyUnexpected(error: unknown): void {
    if (this.wasHandled(error)) {
      return;
    }

    this.messageService.add(this.toUnexpectedToast(error));

    this.markHandled(error);
  }

  /**
   * Exibe um aviso quando as permissões do usuário não puderem ser carregadas.
   *
   * @param error erro técnico ocorrido durante a carga das permissões
   */
  notifyPermissionLoadFailure(error: unknown): void {
    if (this.wasHandled(error)) {
      return;
    }

    this.messageService.add({
      severity: 'warn',
      summary: PERMISSION_ERROR_SUMMARY,
      detail: PERMISSION_ERROR_DETAIL,
    });

    this.markHandled(error);
  }

  /**
   * Informa se o erro já gerou uma notificação visual.
   *
   * @param error erro a ser verificado
   * @returns `true` quando o erro já foi tratado visualmente
   */
  wasHandled(error: unknown): boolean {
    return Boolean(this.errorRecord(error)?.[HANDLED_ERROR]);
  }

  private markHandled(error: unknown): void {
    const record = this.errorRecord(error);

    if (record) {
      record[HANDLED_ERROR] = true;
    }
  }

  private toHttpToast(error: HttpErrorResponse): ToastMessageOptions {
    if (this.isProblemDetail(error.error)) {
      return this.toProblemToast(error.error);
    }

    return {
      severity: error.status >= 500 ? 'error' : 'warn',
      summary: this.defaultHttpSummary(error.status),
      detail: error.message || DEFAULT_ERROR_DETAIL,
    };
  }

  private toProblemToast(problem: ProblemDetail): ToastMessageOptions {
    return {
      severity: this.toSeverity(problem.status),
      summary: problem.title || this.defaultHttpSummary(problem.status),
      detail: this.toProblemDetail(problem),
    };
  }

  private toProblemDetail(problem: ProblemDetail): string {
    if (problem.status === VALIDATION_STATUS && problem.errors?.length) {
      return problem.errors
        .map((error) => error.detail)
        .filter((detail): detail is string => Boolean(detail))
        .join('\n');
    }

    return problem.detail || DEFAULT_ERROR_DETAIL;
  }

  private toUnexpectedDetail(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      return this.toHttpToast(error).detail || DEFAULT_ERROR_DETAIL;
    }

    if (error instanceof AuthorizationError) {
      return ACCESS_DENIED_DETAIL;
    }

    if (error instanceof Error) {
      return error.message || DEFAULT_ERROR_DETAIL;
    }

    return DEFAULT_ERROR_DETAIL;
  }

  private toUnexpectedToast(error: unknown): ToastMessageOptions {
    if (error instanceof AuthorizationError) {
      return {
        severity: 'warn',
        summary: 'Acesso negado',
        detail: ACCESS_DENIED_DETAIL,
      };
    }

    return {
      severity: 'error',
      summary: DEFAULT_ERROR_SUMMARY,
      detail: this.toUnexpectedDetail(error),
    };
  }

  private toSeverity(status?: number): ToastMessageOptions['severity'] {
    if (!status || status >= 500) {
      return 'error';
    }

    return status >= 400 ? 'warn' : 'info';
  }

  private defaultHttpSummary(status?: number): string {
    switch (status) {
      case 401:
        return 'Sessão inválida';
      case 403:
        return 'Acesso negado';
      case 404:
        return 'Recurso não encontrado';
      case 409:
        return 'Regra de negócio violada';
      case 422:
        return 'Requisição inválida';
      default:
        return DEFAULT_ERROR_SUMMARY;
    }
  }

  private isProblemDetail(value: unknown): value is ProblemDetail {
    if (typeof value !== 'object' || value === null) {
      return false;
    }

    return (
      ('status' in value && typeof value.status === 'number') ||
      ('detail' in value && typeof value.detail === 'string') ||
      ('title' in value && typeof value.title === 'string')
    );
  }

  private errorRecord(error: unknown): Record<PropertyKey, unknown> | null {
    return typeof error === 'object' && error !== null
      ? (error as Record<PropertyKey, unknown>)
      : null;
  }
}
