export interface Usuario {
  readonly id: string;
  readonly organizacaoId: string;
  readonly keycloakId: string;
  readonly medicoId?: string | null;
}

export type UsuarioInput = Omit<Usuario, 'id' | 'medicoId'>;
