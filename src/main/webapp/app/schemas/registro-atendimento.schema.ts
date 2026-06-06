export interface RegistroAtendimento {
  readonly id: string;
  readonly consultaId: string;
  readonly medicoId: string;
  readonly queixaPrincipal: string;
  readonly anamnese: string;
  readonly conduta: string;
  readonly observacoes?: string | null;
}

export type RegistroAtendimentoInput = Omit<RegistroAtendimento, 'id'>;
