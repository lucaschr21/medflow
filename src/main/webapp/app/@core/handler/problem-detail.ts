/**
 * Erro de validação retornado pelo backend no formato Problem Details.
 */
export interface ValidationProblemError {
  readonly pointer?: string;
  readonly detail?: string;
  readonly code?: string;
}

/**
 * Contrato RFC 9457 exposto pela API para erros de negócio e validação.
 */
export interface ProblemDetail {
  readonly type?: string;
  readonly title?: string;
  readonly status?: number;
  readonly detail?: string;
  readonly instance?: string;
  readonly errors?: readonly ValidationProblemError[];
}
