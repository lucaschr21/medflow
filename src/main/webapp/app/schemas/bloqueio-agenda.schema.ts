import type { TipoBloqueioAgenda } from './enums/tipo-bloqueio-agenda.enum';

export interface BloqueioAgenda {
  readonly id: string;
  readonly medicoId: string;
  readonly consultorioId: string;
  readonly inicio: string;
  readonly fim: string;
  readonly motivo: string;
  readonly tipo: TipoBloqueioAgenda;
}

export type BloqueioAgendaInput = Omit<BloqueioAgenda, 'id'>;
