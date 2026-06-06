import type { PermissionTuple } from './authorization.types';

/**
 * Erro lançado quando uma operação do frontend exige uma permissão que o
 * usuário atual não possui.
 */
export class AuthorizationError extends Error {
  constructor(readonly permission: PermissionTuple) {
    super(`A operação exige a permissão ${permission[0]}:${permission[1]}.`);
    this.name = 'AuthorizationError';
  }
}
