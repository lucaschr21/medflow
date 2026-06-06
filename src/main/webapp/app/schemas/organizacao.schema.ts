/**
 * Representação de organização devolvida pela API.
 */
export interface Organizacao {
  readonly id: string;
  readonly nome: string;
  readonly email: string;
  readonly telefone: string;
  readonly logotipo: string | null;
  readonly logotipoContentType: string | null;
  readonly corPrimaria: string;
}

/**
 * Payload de entrada usado na criação e atualização de organizações.
 *
 * O campo `logotipo` segue o mesmo contrato do backend, baseado em `byte[]`,
 * e deve ser enviado como string Base64 quando preenchido.
 */
export type OrganizacaoInput = Omit<Organizacao, 'id'>;
