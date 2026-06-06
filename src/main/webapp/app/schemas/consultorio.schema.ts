export interface Consultorio {
  readonly id: string;
  readonly unidadeId: string;
  readonly nome: string;
  readonly sala: string;
}

export type ConsultorioInput = Omit<Consultorio, 'id'>;
