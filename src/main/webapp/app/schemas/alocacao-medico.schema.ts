export interface AlocacaoMedico {
  readonly id: string;
  readonly medicoId: string;
  readonly consultorioId: string;
  readonly dataInicio: string;
  readonly dataFim?: string | null;
}

export type AlocacaoMedicoInput = Omit<AlocacaoMedico, 'id'>;
