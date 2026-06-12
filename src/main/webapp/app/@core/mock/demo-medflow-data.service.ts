import { Injectable, signal, type Signal } from '@angular/core';

import type { FindAllParams } from '../persistence/find-all.params';
import type { PageResult } from '../persistence/page-result';
import type { AlocacaoMedico } from '../../schemas/alocacao-medico.schema';
import type { AgendaMedica } from '../../schemas/agenda-medica.schema';
import type { BloqueioAgenda } from '../../schemas/bloqueio-agenda.schema';
import type { Consulta, ConsultaInput } from '../../schemas/consulta.schema';
import type { Consultorio } from '../../schemas/consultorio.schema';
import type { Especialidade } from '../../schemas/especialidade.schema';
import type { Medico } from '../../schemas/medico.schema';
import type { Organizacao } from '../../schemas/organizacao.schema';
import type { Unidade } from '../../schemas/unidade.schema';
import type { Usuario } from '../../schemas/usuario.schema';

export interface DemoResource<T> {
  readonly value: Signal<T>;
  readonly isLoading: Signal<boolean>;
  reload(): void;
}

interface ConsultaResumo {
  readonly totalHoje: number;
  readonly emEspera: number;
  readonly pendentes: number;
  readonly finalizadas: number;
}

interface RegistroAtendimentoDemo {
  readonly id: string;
  readonly consultaId: string;
  readonly medicoId: string;
  readonly queixaPrincipal: string;
  readonly anamnese: string;
  readonly conduta: string;
  readonly observacoes: string | null;
}

const ORGANIZACAO_ID = 'a0000000-0000-0000-0000-000000000001';
const UNIDADE_MATRIZ_ID = 'b0000000-0000-0000-0000-000000000001';
const UNIDADE_NORTE_ID = 'b0000000-0000-0000-0000-000000000002';
const UNIDADE_SUL_ID = 'b0000000-0000-0000-0000-000000000003';
const CONSULTORIO_101_ID = 'c0000000-0000-0000-0000-000000000001';
const CONSULTORIO_102_ID = 'c0000000-0000-0000-0000-000000000002';
const CONSULTORIO_201_ID = 'c0000000-0000-0000-0000-000000000004';
const CONSULTORIO_202_ID = 'c0000000-0000-0000-0000-000000000005';
const CONSULTORIO_301_ID = 'c0000000-0000-0000-0000-000000000006';
const CONSULTORIO_401_ID = 'c0000000-0000-0000-0000-000000000008';
const ESPECIALIDADE_CARDIO_ID = 'd0000000-0000-0000-0000-000000000001';
const ESPECIALIDADE_DERMA_ID = 'd0000000-0000-0000-0000-000000000002';
const ESPECIALIDADE_GINECO_ID = 'd0000000-0000-0000-0000-000000000003';
const ESPECIALIDADE_NEURO_ID = 'd0000000-0000-0000-0000-000000000004';
const ESPECIALIDADE_PEDIATRIA_ID = 'd0000000-0000-0000-0000-000000000005';
const ESPECIALIDADE_ORTOPEDIA_ID = 'd0000000-0000-0000-0000-000000000006';
const ESPECIALIDADE_OFTALMO_ID = 'd0000000-0000-0000-0000-000000000007';
const ESPECIALIDADE_PSIQUIATRIA_ID = 'd0000000-0000-0000-0000-000000000008';
const ESPECIALIDADE_ENDO_ID = 'd0000000-0000-0000-0000-000000000009';
const ESPECIALIDADE_GASTRO_ID = 'd0000000-0000-0000-0000-000000000010';
const ESPECIALIDADE_URO_ID = 'd0000000-0000-0000-0000-000000000011';
const ESPECIALIDADE_NUTRO_ID = 'd0000000-0000-0000-0000-000000000012';
const USUARIO_ROOT_ID = 'e0000000-0000-0000-0000-000000000001';
const USUARIO_MARIA_ID = 'e0000000-0000-0000-0000-000000000005';
const USUARIO_JOAO_ID = 'e0000000-0000-0000-0000-000000000009';
const USUARIO_CARLA_ID = 'e0000000-0000-0000-0000-000000000010';
const USUARIO_MEDIC_ID = 'e0000000-0000-0000-0000-000000000003';
const USUARIO_MEDIC2_ID = 'e0000000-0000-0000-0000-000000000004';
const USUARIO_MEDIC3_ID = 'e0000000-0000-0000-0000-000000000006';
const USUARIO_MEDIC4_ID = 'e0000000-0000-0000-0000-000000000007';
const USUARIO_MEDIC5_ID = 'e0000000-0000-0000-0000-000000000008';
const MEDICO_CARLOS_ID = 'f0000000-0000-0000-0000-000000000001';
const MEDICO_ANA_ID = 'f0000000-0000-0000-0000-000000000002';
const MEDICO_RICARDO_ID = 'f0000000-0000-0000-0000-000000000003';
const MEDICO_JULIANA_ID = 'f0000000-0000-0000-0000-000000000004';
const MEDICO_PAULO_ID = 'f0000000-0000-0000-0000-000000000005';
const MEDICO_ROOT_ID = 'f0000000-0000-0000-0000-000000000006';
const ALOCACAO_CARLOS_ID = 'a1000000-0000-0000-0000-000000000001';
const ALOCACAO_ANA_ID = 'a1000000-0000-0000-0000-000000000002';
const ALOCACAO_RICARDO_ID = 'a1000000-0000-0000-0000-000000000003';
const ALOCACAO_JULIANA_ID = 'a1000000-0000-0000-0000-000000000004';
const ALOCACAO_PAULO_ID = 'a1000000-0000-0000-0000-000000000005';
const ALOCACAO_ROOT_ID = 'a1000000-0000-0000-0000-000000000006';
const CONSULTA_MARIA_CARDIO_ID = 'a2000000-0000-0000-0000-000000000001';
const CONSULTA_MARIA_NEURO_ID = 'a2000000-0000-0000-0000-000000000002';
const CONSULTA_MARIA_ESPERA_ID = 'a2000000-0000-0000-0000-000000000003';
const CONSULTA_MARIA_ATENDIMENTO_ID = 'a2000000-0000-0000-0000-000000000004';
const CONSULTA_MARIA_GINECO_ID = 'a2000000-0000-0000-0000-000000000005';
const CONSULTA_JOAO_ESPERA_ID = 'a2000000-0000-0000-0000-000000000006';
const CONSULTA_CARLA_ATENDIMENTO_ID = 'a2000000-0000-0000-0000-000000000007';
const CONSULTA_CARLA_FINALIZADA_ID = 'a2000000-0000-0000-0000-000000000008';
const CONSULTA_HISTORICO_ID = 'a2000000-0000-0000-0000-000000000009';
const REGISTRO_HISTORICO_ID = 'a3000000-0000-0000-0000-000000000001';

function pad(value: number): string {
  return value.toString().padStart(2, '0');
}

function localDateTime(offsetDays: number, time: string): string {
  const [hour, minute] = time.split(':').map(Number);
  const date = new Date();
  date.setHours(0, 0, 0, 0);
  date.setDate(date.getDate() + offsetDays);
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(hour)}:${pad(minute)}:00`;
}

function dateOnly(offsetDays: number): string {
  const date = new Date();
  date.setHours(0, 0, 0, 0);
  date.setDate(date.getDate() + offsetDays);
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
}

@Injectable({ providedIn: 'root' })
export class DemoMedflowDataService {
  private readonly organizacoes = signal<Organizacao[]>([
    {
      id: ORGANIZACAO_ID,
      nome: 'Clínica Medflow',
      email: 'contato@medflow.com',
      telefone: '1130000000',
      logotipo: null,
      logotipoContentType: null,
      corPrimaria: '#14b8a6',
    },
  ]);

  private readonly unidades = signal<Unidade[]>([
    {
      id: UNIDADE_MATRIZ_ID,
      organizacaoId: ORGANIZACAO_ID,
      nome: 'Matriz Centro',
      telefone: '1130001000',
      endereco: {
        cep: '01000000',
        logradouro: 'Av. Paulista',
        numero: '1000',
        complemento: '10º andar',
        bairro: 'Bela Vista',
        cidade: 'São Paulo',
        uf: 'SP',
      },
    },
    {
      id: UNIDADE_NORTE_ID,
      organizacaoId: ORGANIZACAO_ID,
      nome: 'Filial Norte',
      telefone: '1130002000',
      endereco: {
        cep: '02000000',
        logradouro: 'Rua Augusta',
        numero: '500',
        complemento: 'Sala 1',
        bairro: 'Consolação',
        cidade: 'São Paulo',
        uf: 'SP',
      },
    },
    {
      id: UNIDADE_SUL_ID,
      organizacaoId: ORGANIZACAO_ID,
      nome: 'Filial Sul',
      telefone: '1130003000',
      endereco: {
        cep: '03000000',
        logradouro: 'Av. Santo Amaro',
        numero: '700',
        complemento: 'Térreo',
        bairro: 'Santo Amaro',
        cidade: 'São Paulo',
        uf: 'SP',
      },
    },
  ]);

  private readonly consultorios = signal<Consultorio[]>([
    { id: CONSULTORIO_101_ID, unidadeId: UNIDADE_MATRIZ_ID, nome: 'Consultório 101', sala: '101' },
    { id: CONSULTORIO_102_ID, unidadeId: UNIDADE_MATRIZ_ID, nome: 'Consultório 102', sala: '102' },
    { id: CONSULTORIO_201_ID, unidadeId: UNIDADE_NORTE_ID, nome: 'Consultório 201', sala: '201' },
    { id: CONSULTORIO_202_ID, unidadeId: UNIDADE_NORTE_ID, nome: 'Consultório 202', sala: '202' },
    { id: CONSULTORIO_301_ID, unidadeId: UNIDADE_SUL_ID, nome: 'Consultório 301', sala: '301' },
    { id: CONSULTORIO_401_ID, unidadeId: UNIDADE_SUL_ID, nome: 'Consultório 401', sala: '401' },
  ]);

  private readonly especialidades = signal<Especialidade[]>([
    { id: ESPECIALIDADE_CARDIO_ID, nome: 'Cardiologia', descricao: 'Doenças do coração e sistema circulatório' },
    { id: ESPECIALIDADE_DERMA_ID, nome: 'Dermatologia', descricao: 'Cuidados com pele, cabelos e unhas' },
    { id: ESPECIALIDADE_GINECO_ID, nome: 'Ginecologia', descricao: 'Saúde da mulher e sistema reprodutor' },
    { id: ESPECIALIDADE_NEURO_ID, nome: 'Neurologia', descricao: 'Distúrbios do sistema nervoso' },
    { id: ESPECIALIDADE_PEDIATRIA_ID, nome: 'Pediatria', descricao: 'Saúde de crianças e adolescentes' },
    { id: ESPECIALIDADE_ORTOPEDIA_ID, nome: 'Ortopedia', descricao: 'Ossos, articulações e músculos' },
    { id: ESPECIALIDADE_OFTALMO_ID, nome: 'Oftalmologia', descricao: 'Doenças oculares e visão' },
    { id: ESPECIALIDADE_PSIQUIATRIA_ID, nome: 'Psiquiatria', descricao: 'Saúde mental' },
    { id: ESPECIALIDADE_ENDO_ID, nome: 'Endocrinologia', descricao: 'Distúrbios hormonais e metabólicos' },
    { id: ESPECIALIDADE_GASTRO_ID, nome: 'Gastroenterologia', descricao: 'Doenças do aparelho digestivo' },
    { id: ESPECIALIDADE_URO_ID, nome: 'Urologia', descricao: 'Sistema urinário e reprodutor masculino' },
    { id: ESPECIALIDADE_NUTRO_ID, nome: 'Nutrologia', descricao: 'Nutrição clínica e dietoterapia' },
  ]);

  private readonly usuarios = signal<Usuario[]>([
    { id: USUARIO_ROOT_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: 'd5ed50a4-519a-4f49-ace7-3d1206e88811', medicoId: MEDICO_ROOT_ID },
    { id: USUARIO_MEDIC_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: 'a72c9062-82ba-4795-abf6-bf4c2c0e93c8', medicoId: MEDICO_CARLOS_ID },
    { id: USUARIO_MEDIC2_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: 'b72d51a3-93ca-5806-bc07-cf3d3d1e04d9', medicoId: MEDICO_ANA_ID },
    { id: USUARIO_MEDIC3_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: 'c83e62b4-a4db-6917-cd18-df4e4e2f15e0', medicoId: MEDICO_RICARDO_ID },
    { id: USUARIO_MEDIC4_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: 'd94f73c5-b5ec-7a28-de29-ef5f5f3a26f1', medicoId: MEDICO_JULIANA_ID },
    { id: USUARIO_MEDIC5_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: 'e05a84d6-c6fd-8b39-ef30-fa6a6a4b37a2', medicoId: MEDICO_PAULO_ID },
    { id: 'e0000000-0000-0000-0000-000000000002', organizacaoId: ORGANIZACAO_ID, keycloakId: 'e8fb3e37-f185-48ef-9a28-7da3063f3a3c', medicoId: null },
    { id: USUARIO_JOAO_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: '519e25e2-dd01-556c-ce39-ce2832210e32', medicoId: null },
    { id: USUARIO_CARLA_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: '62af36f3-ee12-667d-df40-df3943321f43', medicoId: null },
    { id: USUARIO_MARIA_ID, organizacaoId: ORGANIZACAO_ID, keycloakId: '408d14d1-cc8f-445b-bd2c-bd1721109d21', medicoId: null },
  ]);

  private readonly medicos = signal<Medico[]>([
    { id: MEDICO_CARLOS_ID, usuarioId: USUARIO_MEDIC_ID, especialidadeIds: [ESPECIALIDADE_CARDIO_ID, ESPECIALIDADE_NEURO_ID] },
    { id: MEDICO_ANA_ID, usuarioId: USUARIO_MEDIC2_ID, especialidadeIds: [ESPECIALIDADE_GINECO_ID, ESPECIALIDADE_PEDIATRIA_ID] },
    { id: MEDICO_RICARDO_ID, usuarioId: USUARIO_MEDIC3_ID, especialidadeIds: [ESPECIALIDADE_ORTOPEDIA_ID, ESPECIALIDADE_DERMA_ID] },
    { id: MEDICO_JULIANA_ID, usuarioId: USUARIO_MEDIC4_ID, especialidadeIds: [ESPECIALIDADE_OFTALMO_ID, ESPECIALIDADE_ENDO_ID] },
    { id: MEDICO_PAULO_ID, usuarioId: USUARIO_MEDIC5_ID, especialidadeIds: [ESPECIALIDADE_PSIQUIATRIA_ID, ESPECIALIDADE_GASTRO_ID] },
    { id: MEDICO_ROOT_ID, usuarioId: USUARIO_ROOT_ID, especialidadeIds: [ESPECIALIDADE_URO_ID, ESPECIALIDADE_NUTRO_ID] },
  ]);

  private readonly alocacoesMedicas = signal<AlocacaoMedico[]>([
    { id: ALOCACAO_CARLOS_ID, medicoId: MEDICO_CARLOS_ID, consultorioId: CONSULTORIO_101_ID, dataInicio: '2026-01-01', dataFim: null },
    { id: ALOCACAO_ANA_ID, medicoId: MEDICO_ANA_ID, consultorioId: CONSULTORIO_201_ID, dataInicio: '2026-01-01', dataFim: null },
    { id: ALOCACAO_RICARDO_ID, medicoId: MEDICO_RICARDO_ID, consultorioId: CONSULTORIO_301_ID, dataInicio: '2026-01-01', dataFim: null },
    { id: ALOCACAO_JULIANA_ID, medicoId: MEDICO_JULIANA_ID, consultorioId: CONSULTORIO_401_ID, dataInicio: '2026-01-01', dataFim: null },
    { id: ALOCACAO_PAULO_ID, medicoId: MEDICO_PAULO_ID, consultorioId: CONSULTORIO_202_ID, dataInicio: '2026-01-01', dataFim: null },
    { id: ALOCACAO_ROOT_ID, medicoId: MEDICO_ROOT_ID, consultorioId: CONSULTORIO_102_ID, dataInicio: '2026-01-01', dataFim: null },
  ]);

  private readonly agendasMedicas = signal<AgendaMedica[]>([
    { id: 'g0000000-0000-0000-0000-000000000001', alocacaoMedicoId: ALOCACAO_CARLOS_ID, diaSemana: 'MONDAY', horaInicio: '08:00', horaFim: '17:00' },
    { id: 'g0000000-0000-0000-0000-000000000002', alocacaoMedicoId: ALOCACAO_CARLOS_ID, diaSemana: 'TUESDAY', horaInicio: '08:00', horaFim: '17:00' },
    { id: 'g0000000-0000-0000-0000-000000000003', alocacaoMedicoId: ALOCACAO_CARLOS_ID, diaSemana: 'WEDNESDAY', horaInicio: '08:00', horaFim: '17:00' },
    { id: 'g0000000-0000-0000-0000-000000000004', alocacaoMedicoId: ALOCACAO_ANA_ID, diaSemana: 'MONDAY', horaInicio: '09:00', horaFim: '18:00' },
    { id: 'g0000000-0000-0000-0000-000000000005', alocacaoMedicoId: ALOCACAO_ANA_ID, diaSemana: 'TUESDAY', horaInicio: '09:00', horaFim: '18:00' },
    { id: 'g0000000-0000-0000-0000-000000000006', alocacaoMedicoId: ALOCACAO_RICARDO_ID, diaSemana: 'FRIDAY', horaInicio: '07:00', horaFim: '13:00' },
  ]);

  private readonly bloqueiosAgenda = signal<BloqueioAgenda[]>([
    {
      id: 'h0000000-0000-0000-0000-000000000001',
      medicoId: MEDICO_CARLOS_ID,
      consultorioId: CONSULTORIO_101_ID,
      inicio: localDateTime(0, '12:00'),
      fim: localDateTime(0, '14:00'),
      motivo: 'Reunião clínica semanal',
      tipo: 'PAUSA',
    },
    {
      id: 'h0000000-0000-0000-0000-000000000002',
      medicoId: MEDICO_ANA_ID,
      consultorioId: CONSULTORIO_201_ID,
      inicio: localDateTime(1, '08:00'),
      fim: localDateTime(1, '18:00'),
      motivo: 'Congresso de pediatria',
      tipo: 'INDISPONIBILIDADE',
    },
    {
      id: 'h0000000-0000-0000-0000-000000000003',
      medicoId: MEDICO_PAULO_ID,
      consultorioId: CONSULTORIO_202_ID,
      inicio: localDateTime(3, '13:00'),
      fim: localDateTime(3, '20:00'),
      motivo: 'Férias',
      tipo: 'FERIAS',
    },
  ]);

  private readonly consultas = signal<Consulta[]>([
    {
      id: CONSULTA_MARIA_CARDIO_ID,
      usuarioId: USUARIO_MARIA_ID,
      medicoId: MEDICO_CARLOS_ID,
      consultorioId: CONSULTORIO_101_ID,
      alocacaoMedicoId: ALOCACAO_CARLOS_ID,
      dataHoraInicio: localDateTime(0, '09:00'),
      dataHoraFim: localDateTime(0, '09:30'),
      status: 'AGENDADA',
      tipoConsulta: 'Cardiologia',
      motivo: 'Check-up de rotina',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_MARIA_NEURO_ID,
      usuarioId: USUARIO_MARIA_ID,
      medicoId: MEDICO_CARLOS_ID,
      consultorioId: CONSULTORIO_101_ID,
      alocacaoMedicoId: ALOCACAO_CARLOS_ID,
      dataHoraInicio: localDateTime(0, '10:00'),
      dataHoraFim: localDateTime(0, '10:30'),
      status: 'CONFIRMADA',
      tipoConsulta: 'Neurologia',
      motivo: 'Dor de cabeça frequente',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_MARIA_ESPERA_ID,
      usuarioId: USUARIO_MARIA_ID,
      medicoId: MEDICO_CARLOS_ID,
      consultorioId: CONSULTORIO_101_ID,
      alocacaoMedicoId: ALOCACAO_CARLOS_ID,
      dataHoraInicio: localDateTime(0, '14:00'),
      dataHoraFim: localDateTime(0, '14:30'),
      status: 'EM_ESPERA',
      tipoConsulta: 'Cardiologia',
      motivo: 'Retorno de exames',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_MARIA_ATENDIMENTO_ID,
      usuarioId: USUARIO_MARIA_ID,
      medicoId: MEDICO_CARLOS_ID,
      consultorioId: CONSULTORIO_101_ID,
      alocacaoMedicoId: ALOCACAO_CARLOS_ID,
      dataHoraInicio: localDateTime(0, '15:00'),
      dataHoraFim: localDateTime(0, '15:30'),
      status: 'EM_ATENDIMENTO',
      tipoConsulta: 'Neurologia',
      motivo: 'Formigamento nos braços',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_MARIA_GINECO_ID,
      usuarioId: USUARIO_MARIA_ID,
      medicoId: MEDICO_ANA_ID,
      consultorioId: CONSULTORIO_201_ID,
      alocacaoMedicoId: ALOCACAO_ANA_ID,
      dataHoraInicio: localDateTime(-1, '11:00'),
      dataHoraFim: localDateTime(-1, '11:30'),
      status: 'FINALIZADA',
      tipoConsulta: 'Ginecologia',
      motivo: 'Exame preventivo',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_JOAO_ESPERA_ID,
      usuarioId: USUARIO_JOAO_ID,
      medicoId: MEDICO_CARLOS_ID,
      consultorioId: CONSULTORIO_101_ID,
      alocacaoMedicoId: ALOCACAO_CARLOS_ID,
      dataHoraInicio: localDateTime(0, '16:00'),
      dataHoraFim: localDateTime(0, '16:30'),
      status: 'EM_ESPERA',
      tipoConsulta: 'Cardiologia',
      motivo: 'Pressão alta',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_CARLA_ATENDIMENTO_ID,
      usuarioId: USUARIO_CARLA_ID,
      medicoId: MEDICO_ANA_ID,
      consultorioId: CONSULTORIO_201_ID,
      alocacaoMedicoId: ALOCACAO_ANA_ID,
      dataHoraInicio: localDateTime(0, '10:30'),
      dataHoraFim: localDateTime(0, '11:00'),
      status: 'EM_ATENDIMENTO',
      tipoConsulta: 'Pediatria',
      motivo: 'Vacinação e acompanhamento',
      registroAtendimentoId: null,
    },
    {
      id: CONSULTA_CARLA_FINALIZADA_ID,
      usuarioId: USUARIO_CARLA_ID,
      medicoId: MEDICO_ANA_ID,
      consultorioId: CONSULTORIO_201_ID,
      alocacaoMedicoId: ALOCACAO_ANA_ID,
      dataHoraInicio: localDateTime(-1, '15:00'),
      dataHoraFim: localDateTime(-1, '15:30'),
      status: 'FINALIZADA',
      tipoConsulta: 'Ginecologia',
      motivo: 'Revisão pós-consulta',
      registroAtendimentoId: REGISTRO_HISTORICO_ID,
    },
    {
      id: CONSULTA_HISTORICO_ID,
      usuarioId: USUARIO_JOAO_ID,
      medicoId: MEDICO_RICARDO_ID,
      consultorioId: CONSULTORIO_301_ID,
      alocacaoMedicoId: ALOCACAO_RICARDO_ID,
      dataHoraInicio: localDateTime(-3, '09:00'),
      dataHoraFim: localDateTime(-3, '09:30'),
      status: 'FINALIZADA',
      tipoConsulta: 'Dermatologia',
      motivo: 'Manchas na pele',
      registroAtendimentoId: null,
    },
  ]);

  private readonly registrosAtendimento = signal<RegistroAtendimentoDemo[]>([
    {
      id: REGISTRO_HISTORICO_ID,
      consultaId: CONSULTA_CARLA_FINALIZADA_ID,
      medicoId: MEDICO_ANA_ID,
      queixaPrincipal: 'Retorno de prevenção',
      anamnese: 'Paciente compareceu sem queixas agudas, referindo apenas necessidade de revisão de rotina e atualização de exames.',
      conduta: 'Orientado seguimento anual, manutenção de rotina de exames e retorno se houver novos sintomas.',
      observacoes: 'Sem intercorrências durante o atendimento.',
    },
  ]);

  private readonly anexoConsulta = signal<never[]>([]);

  createResource<T>(selector: () => T): DemoResource<T> {
    const value = signal(selector());
    const isLoading = signal(false);
    return {
      value,
      isLoading,
      reload: () => value.set(selector()),
    };
  }

  pageOf<T>(items: readonly T[], params: FindAllParams = {}): PageResult<T> {
    const size = params.size && params.size > 0 ? params.size : items.length || 10;
    const page = params.page && params.page > 0 ? params.page : 0;
    const start = page * size;
    const content = items.slice(start, start + size);
    return {
      content,
      page,
      size,
      totalElements: items.length,
      totalPages: Math.max(1, Math.ceil(items.length / size)),
    };
  }

  list(resource: string, params: FindAllParams = {}): PageResult<unknown> {
    switch (resource) {
      case 'organizacao':
        return this.pageOf(this.organizacoes(), params);
      case 'unidade':
        return this.pageOf(this.unidades(), params);
      case 'consultorio':
        return this.pageOf(this.consultorios(), params);
      case 'especialidade':
        return this.pageOf(this.especialidades(), params);
      case 'usuario':
        return this.pageOf(this.usuarios(), params);
      case 'medico':
        return this.pageOf(this.medicos(), params);
      case 'alocacao-medico':
        return this.pageOf(this.alocacoesMedicas(), params);
      case 'agenda-medica':
        return this.pageOf(this.agendasMedicas(), params);
      case 'bloqueio-agenda':
        return this.pageOf(this.bloqueiosAgenda(), params);
      case 'consulta':
        return this.pageOf(this.consultas(), params);
      case 'registro-atendimento':
        return this.pageOf(this.registrosAtendimento(), params);
      case 'anexo-consulta':
        return this.pageOf(this.anexoConsulta(), params);
      default:
        return this.pageOf([], params);
    }
  }

  findById(resource: string, id: string): unknown {
    return this.findAllItems(resource).find((item) => item.id === id) ?? null;
  }

  create(resource: string, input: unknown): unknown {
    switch (resource) {
      case 'organizacao':
        return this.insert(this.organizacoes, { ...(input as Partial<Organizacao>), id: this.randomId('o') } as Organizacao);
      case 'unidade':
        return this.insert(this.unidades, { ...(input as Partial<Unidade>), id: this.randomId('u') } as Unidade);
      case 'consultorio':
        return this.insert(this.consultorios, { ...(input as Partial<Consultorio>), id: this.randomId('c') } as Consultorio);
      case 'especialidade':
        return this.insert(this.especialidades, { ...(input as Partial<Especialidade>), id: this.randomId('e') } as Especialidade);
      case 'usuario':
        return this.insert(this.usuarios, {
          ...(input as Partial<Usuario>),
          id: this.randomId('u'),
          organizacaoId: ORGANIZACAO_ID,
          keycloakId: this.randomUuid(),
          medicoId: null,
        } as Usuario);
      case 'medico':
        return this.insert(this.medicos, { ...(input as Partial<Medico>), id: this.randomId('m') } as Medico);
      case 'alocacao-medico':
        return this.insert(this.alocacoesMedicas, { ...(input as Partial<AlocacaoMedico>), id: this.randomId('a') } as AlocacaoMedico);
      case 'agenda-medica':
        return this.insert(this.agendasMedicas, { ...(input as Partial<AgendaMedica>), id: this.randomId('g') } as AgendaMedica);
      case 'bloqueio-agenda':
        return this.insert(this.bloqueiosAgenda, { ...(input as Partial<BloqueioAgenda>), id: this.randomId('b') } as BloqueioAgenda);
      case 'consulta':
        return this.insert(this.consultas, {
          ...(input as Partial<Consulta>),
          id: this.randomId('k'),
          registroAtendimentoId: null,
        } as Consulta);
      default:
        return input;
    }
  }

  update(resource: string, id: string, input: unknown): unknown {
    switch (resource) {
      case 'organizacao':
        return this.merge(this.organizacoes, id, input as Partial<Organizacao>);
      case 'unidade':
        return this.merge(this.unidades, id, input as Partial<Unidade>);
      case 'consultorio':
        return this.merge(this.consultorios, id, input as Partial<Consultorio>);
      case 'especialidade':
        return this.merge(this.especialidades, id, input as Partial<Especialidade>);
      case 'usuario':
        return this.merge(this.usuarios, id, input as Partial<Usuario>);
      case 'medico':
        return this.merge(this.medicos, id, input as Partial<Medico>);
      case 'alocacao-medico':
        return this.merge(this.alocacoesMedicas, id, input as Partial<AlocacaoMedico>);
      case 'agenda-medica':
        return this.merge(this.agendasMedicas, id, input as Partial<AgendaMedica>);
      case 'bloqueio-agenda':
        return this.merge(this.bloqueiosAgenda, id, input as Partial<BloqueioAgenda>);
      case 'consulta':
        return this.merge(this.consultas, id, input as Partial<Consulta>);
      default:
        return input;
    }
  }

  remove(resource: string, id: string): void {
    switch (resource) {
      case 'organizacao':
        this.removeFrom(this.organizacoes, id);
        break;
      case 'unidade':
        this.removeFrom(this.unidades, id);
        break;
      case 'consultorio':
        this.removeFrom(this.consultorios, id);
        break;
      case 'especialidade':
        this.removeFrom(this.especialidades, id);
        break;
      case 'usuario':
        this.removeFrom(this.usuarios, id);
        break;
      case 'medico':
        this.removeFrom(this.medicos, id);
        break;
      case 'alocacao-medico':
        this.removeFrom(this.alocacoesMedicas, id);
        break;
      case 'agenda-medica':
        this.removeFrom(this.agendasMedicas, id);
        break;
      case 'bloqueio-agenda':
        this.removeFrom(this.bloqueiosAgenda, id);
        break;
      case 'consulta':
        this.removeFrom(this.consultas, id);
        break;
      default:
        break;
    }
  }

  consultaResumoHoje(medicoId?: string | null): ConsultaResumo {
    const hoje = dateOnly(0);
    const consultas = this.consultas().filter((consulta) => consulta.dataHoraInicio.startsWith(hoje));
    const filtradas = medicoId ? consultas.filter((consulta) => consulta.medicoId === medicoId) : consultas;
    return {
      totalHoje: filtradas.length,
      emEspera: filtradas.filter((consulta) => consulta.status === 'EM_ESPERA').length,
      pendentes: filtradas.filter((consulta) => consulta.status === 'AGENDADA' || consulta.status === 'CONFIRMADA').length,
      finalizadas: filtradas.filter((consulta) => consulta.status === 'FINALIZADA').length,
    };
  }

  consultasDoMedicoHoje(medicoId: string): Consulta[] {
    const hoje = dateOnly(0);
    return this.consultas().filter(
      (consulta) => consulta.medicoId === medicoId && consulta.dataHoraInicio.startsWith(hoje),
    );
  }

  consultasHoje(): Consulta[] {
    const hoje = dateOnly(0);
    return this.consultas().filter((consulta) => consulta.dataHoraInicio.startsWith(hoje));
  }

  consultasDoMedicoFila(medicoId: string): Consulta[] {
    return this.consultas().filter(
      (consulta) => consulta.medicoId === medicoId && consulta.status === 'EM_ESPERA',
    );
  }

  consultasDoUsuario(keycloakId: string | null): Consulta[] {
    const usuario = this.usuarios().find((item) => item.keycloakId === keycloakId);
    if (!usuario) {
      return [];
    }
    return this.consultas().filter((consulta) => consulta.usuarioId === usuario.id);
  }

  findUsuarioByKeycloakId(keycloakId: string | null): Usuario | null {
    if (!keycloakId) {
      return null;
    }
    return this.usuarios().find((usuario) => usuario.keycloakId === keycloakId) ?? null;
  }

  findMedicoByKeycloakId(keycloakId: string | null): Medico | null {
    const usuario = this.findUsuarioByKeycloakId(keycloakId);
    if (!usuario?.medicoId) {
      return null;
    }
    return this.medicos().find((medico) => medico.id === usuario.medicoId) ?? null;
  }

  findAlocacaoByMedicoId(medicoId: string | null): AlocacaoMedico | null {
    if (!medicoId) {
      return null;
    }
    return this.alocacoesMedicas().find((alocacao) => alocacao.medicoId === medicoId) ?? null;
  }

  markCheckIn(consultaId: string): Consulta | null {
    return this.updateConsultaStatus(consultaId, 'CONFIRMADA');
  }

  markEmEspera(consultaId: string): Consulta | null {
    return this.updateConsultaStatus(consultaId, 'EM_ESPERA');
  }

  markIniciarAtendimento(consultaId: string): Consulta | null {
    return this.updateConsultaStatus(consultaId, 'EM_ATENDIMENTO');
  }

  markFinalizar(consultaId: string): Consulta | null {
    return this.updateConsultaStatus(consultaId, 'FINALIZADA');
  }

  markNaoCompareceu(consultaId: string): Consulta | null {
    return this.updateConsultaStatus(consultaId, 'NAO_COMPARECEU');
  }

  markCancelar(consultaId: string): Consulta | null {
    return this.updateConsultaStatus(consultaId, 'CANCELADA');
  }

  agendarConsulta(
    input: ConsultaInput,
    usuarioId: string,
    consultorioId: string,
    alocacaoMedicoId: string,
  ): Consulta {
    const entity: Consulta = {
      ...input,
      id: this.randomId('k'),
      usuarioId,
      consultorioId,
      alocacaoMedicoId,
      registroAtendimentoId: null,
    };
    this.consultas.update((items) => [entity, ...items]);
    return entity;
  }

  criarRegistroAtendimento(
    consultaId: string,
    registro: { medicoId: string; queixaPrincipal: string; anamnese: string; conduta: string; observacoes?: string | null },
  ) {
    const entity = {
      id: this.randomId('r'),
      consultaId,
      medicoId: registro.medicoId,
      queixaPrincipal: registro.queixaPrincipal,
      anamnese: registro.anamnese,
      conduta: registro.conduta,
      observacoes: registro.observacoes ?? null,
    };
    this.registrosAtendimento.update((items) => [entity as RegistroAtendimentoDemo, ...items]);
    this.consultas.update((items) =>
      items.map((consulta) =>
        consulta.id === consultaId ? { ...consulta, registroAtendimentoId: entity.id, status: 'FINALIZADA' } : consulta,
      ),
    );
    return entity;
  }

  private findAllItems(resource: string): readonly { id: string }[] {
    switch (resource) {
      case 'organizacao':
        return this.organizacoes();
      case 'unidade':
        return this.unidades();
      case 'consultorio':
        return this.consultorios();
      case 'especialidade':
        return this.especialidades();
      case 'usuario':
        return this.usuarios();
      case 'medico':
        return this.medicos();
      case 'alocacao-medico':
        return this.alocacoesMedicas();
      case 'agenda-medica':
        return this.agendasMedicas();
      case 'bloqueio-agenda':
        return this.bloqueiosAgenda();
      case 'consulta':
        return this.consultas();
      case 'registro-atendimento':
        return this.registrosAtendimento();
      case 'anexo-consulta':
        return this.anexoConsulta();
      default:
        return [];
    }
  }

  private updateConsultaStatus(consultaId: string, status: Consulta['status']): Consulta | null {
    let updated: Consulta | null = null;
    this.consultas.update((items) =>
      items.map((consulta) => {
        if (consulta.id !== consultaId) {
          return consulta;
        }
        updated = { ...consulta, status };
        return updated;
      }),
    );
    return updated;
  }

  private insert<T extends { id: string }>(store: { update: (fn: (items: readonly T[]) => T[]) => void }, entity: T): T {
    store.update((items) => [entity, ...items]);
    return entity;
  }

  private merge<T extends { id: string }>(store: { update: (fn: (items: readonly T[]) => T[]) => void }, id: string, input: Partial<T>): T | null {
    let updated: T | null = null;
    store.update((items) =>
      items.map((item) => {
        if (item.id !== id) {
          return item;
        }
        updated = { ...item, ...input, id };
        return updated;
      }),
    );
    return updated;
  }

  private removeFrom<T extends { id: string }>(store: { update: (fn: (items: readonly T[]) => T[]) => void }, id: string): void {
    store.update((items) => items.filter((item) => item.id !== id));
  }

  private randomId(prefix: string): string {
    return `${prefix}${globalThis.crypto?.randomUUID?.() ?? Math.random().toString(16).slice(2)}`;
  }

  private randomUuid(): string {
    return globalThis.crypto?.randomUUID?.() ?? `${Date.now()}-${Math.random()}`;
  }
}
