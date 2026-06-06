export interface Medico {
  readonly id: string;
  readonly usuarioId: string;
  readonly especialidadeIds: string[];
}

export type MedicoInput = Omit<Medico, 'id'>;
