import type { StatusConsulta } from './enums/status-consulta.enum';

export interface Consulta {
  readonly id: string;
  readonly usuarioId: string;
  readonly medicoId: string;
  readonly consultorioId: string;
  readonly alocacaoMedicoId: string;
  readonly dataHoraInicio: string;
  readonly dataHoraFim: string;
  readonly status: StatusConsulta;
  readonly tipoConsulta: string;
  readonly motivo: string;
  readonly registroAtendimentoId?: string | null;
}

export type ConsultaInput = Omit<Consulta, 'id' | 'registroAtendimentoId'>;
