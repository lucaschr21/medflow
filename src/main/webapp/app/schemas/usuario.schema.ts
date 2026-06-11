export interface Usuario {
  readonly id: string;
  readonly organizacaoId: string;
  readonly keycloakId: string;
  readonly medicoId?: string | null;
}

/**
 * Schema de criação de usuário — compatível com POST /api/usuarios.
 * O backend cria o usuário no Keycloak automaticamente.
 */
export interface UsuarioInput {
  organizacaoId: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  cpf: string;
  telefone: string;
  dataNascimento: string;
  tipoAcesso: string;
}
