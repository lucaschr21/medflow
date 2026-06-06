export interface AnexoConsulta {
  readonly id: string;
  readonly consultaId: string;
  readonly nomeArquivo: string;
  readonly contentType: string;
  readonly tamanhoBytes: number;
  readonly descricao?: string | null;
}

export interface AnexoConsultaInput extends Omit<AnexoConsulta, 'id'> {
  readonly arquivo: string;
}
