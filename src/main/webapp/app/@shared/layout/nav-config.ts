import type { UserRole } from '../../@core/security/authentication/authentication.service';
import type { PermissionTuple } from '../../@core/security/authorization/authorization.types';

export interface NavItem {
  readonly label: string;
  readonly icon: string;
  readonly href: string;
  readonly permission?: PermissionTuple;
}

export interface NavGroup {
  readonly label?: string;
  readonly items: readonly NavItem[];
}

const NAV_BY_ROLE: Record<UserRole, readonly NavGroup[]> = {
  USUARIO: [
    {
      items: [
        { label: 'Início', icon: 'pi pi-home', href: '/' },
        {
          label: 'Agendar consulta',
          icon: 'pi pi-calendar-plus',
          href: '/agendar-consulta',
          permission: ['consulta', 'create'],
        },
        {
          label: 'Minhas consultas',
          icon: 'pi pi-calendar',
          href: '/minhas-consultas',
          permission: ['consulta', 'read'],
        },
      ],
    },
  ],

  MEDICO: [
    {
      label: 'Atendimento',
      items: [
        { label: 'Minha agenda', icon: 'pi pi-calendar', href: '/' },
        {
          label: 'Fila de atendimento',
          icon: 'pi pi-list-check',
          href: '/fila',
          permission: ['consulta', 'read'],
        },
      ],
    },
    {
      label: 'Agenda',
      items: [
        {
          label: 'Meus bloqueios',
          icon: 'pi pi-ban',
          href: '/bloqueios-agenda',
          permission: ['bloqueio-agenda', 'create'],
        },
      ],
    },
  ],

  RECEPCIONISTA: [
    {
      label: 'Operacional',
      items: [
        {
          label: 'Agenda do dia',
          icon: 'pi pi-calendar',
          href: '/',
          permission: ['consulta', 'read'],
        },
        {
          label: 'Fila de atendimento',
          icon: 'pi pi-list-check',
          href: '/fila',
          permission: ['consulta', 'read'],
        },
      ],
    },
    {
      label: 'Consultas',
      items: [
        {
          label: 'Nova consulta',
          icon: 'pi pi-calendar-plus',
          href: '/agendar-consulta',
          permission: ['consulta', 'create'],
        },
        {
          label: 'Todas as consultas',
          icon: 'pi pi-list',
          href: '/consultas',
          permission: ['consulta', 'read'],
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
      ],
    },
  ],

  ADMINISTRADOR: [
    {
      label: 'Visão geral',
      items: [{ label: 'Dashboard', icon: 'pi pi-home', href: '/' }],
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
          label: 'Alocações',
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
          label: 'Bloqueios',
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
          icon: 'pi pi-building',
          href: '/organizacoes',
          permission: ['organizacao', 'read'],
        },
      ],
    },
  ],
};

export function getNavGroups(role: UserRole | null): readonly NavGroup[] {
  if (!role) return [];
  return NAV_BY_ROLE[role] ?? [];
}
