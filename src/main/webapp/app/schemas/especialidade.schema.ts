export interface Especialidade {
  readonly id: string;
  readonly nome: string;
  readonly descricao?: string | null;
}

export type EspecialidadeInput = Omit<Especialidade, 'id'>;
