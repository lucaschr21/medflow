import type { PermissionTuple } from '../../@core/security/authorization/authorization.types';

export interface NavItem {
  readonly label: string;
  readonly icon: string;
  readonly href?: string;
  readonly permission?: PermissionTuple;
}

export interface NavGroup {
  readonly label: string;
  readonly items: readonly NavItem[];
}

export const NAV_GROUPS: readonly NavGroup[] = [
  {
    label: 'Atendimento',
    items: [
      {
        label: 'Consultas',
        icon: 'pi pi-calendar',
        href: '/consultas',
        permission: ['consulta', 'read'],
      },
      {
        label: 'Registro de atendimento',
        icon: 'pi pi-file-edit',
        href: '/registros-atendimento',
        permission: ['registro-atendimento', 'read'],
      },
      {
        label: 'Anexos de consulta',
        icon: 'pi pi-paperclip',
        href: '/anexos-consulta',
        permission: ['anexo-consulta', 'read'],
      },
    ],
  },
  {
    label: 'Cadastros',
    items: [
      {
        label: 'Usuários',
        icon: 'pi pi-users',
        href: '/usuarios',
        permission: ['usuario', 'read'],
      },
      {
        label: 'Médicos',
        icon: 'pi pi-user-plus',
        href: '/medicos',
        permission: ['medico', 'read'],
      },
      {
        label: 'Especialidades',
        icon: 'pi pi-book',
        href: '/especialidades',
        permission: ['especialidade', 'read'],
      },
      {
        label: 'Unidades',
        icon: 'pi pi-building',
        href: '/unidades',
        permission: ['unidade', 'read'],
      },
      {
        label: 'Consultórios',
        icon: 'pi pi-clone',
        href: '/consultorios',
        permission: ['consultorio', 'read'],
      },
    ],
  },
  {
    label: 'Agenda',
    items: [
      {
        label: 'Alocações médicas',
        icon: 'pi pi-sitemap',
        href: '/alocacoes-medicas',
        permission: ['alocacao-medico', 'read'],
      },
      {
        label: 'Agenda médica',
        icon: 'pi pi-clock',
        href: '/agendas-medicas',
        permission: ['agenda-medica', 'read'],
      },
      {
        label: 'Bloqueios de agenda',
        icon: 'pi pi-ban',
        href: '/bloqueios-agenda',
        permission: ['bloqueio-agenda', 'read'],
      },
    ],
  },
  {
    label: 'Administração',
    items: [
      {
        label: 'Organização',
        icon: 'pi pi-briefcase',
        href: '/organizacoes',
        permission: ['organizacao', 'read'],
      },
    ],
  },
] as const;
