import type { Endereco } from './endereco.schema';

export interface Unidade {
  readonly id: string;
  readonly organizacaoId: string;
  readonly nome: string;
  readonly telefone: string;
  readonly endereco: Endereco;
}

export type UnidadeInput = Omit<Unidade, 'id'>;
