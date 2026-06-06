import type { Uf } from './enums/uf.enum';

export interface Endereco {
  readonly logradouro: string;
  readonly numero: string;
  readonly bairro: string;
  readonly cidade: string;
  readonly uf: Uf;
  readonly cep: string;
  readonly complemento?: string | null;
}
