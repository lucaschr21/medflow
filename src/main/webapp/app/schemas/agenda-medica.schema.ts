export interface AgendaMedica {
  readonly id: string;
  readonly alocacaoMedicoId: string;
  readonly diaSemana: string;
  readonly horaInicio: string;
  readonly horaFim: string;
}

export type AgendaMedicaInput = Omit<AgendaMedica, 'id'>;
