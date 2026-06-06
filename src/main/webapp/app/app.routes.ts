import type { Routes } from '@angular/router';
import { authorizationGuard } from './@core/security/authorization/authorization.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./@shared/layout/app-shell/app-shell').then((m) => m.AppShell),
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./pages/default-route/default-route-page').then((m) => m.DefaultRoutePage),
      },
      {
        path: 'organizacoes',
        canActivate: [authorizationGuard(['organizacao', 'read'])],
        loadComponent: () =>
          import('./pages/organizacoes/organizacoes-page').then((m) => m.OrganizacoesPage),
      },
      {
        path: 'unidades',
        canActivate: [authorizationGuard(['unidade', 'read'])],
        loadComponent: () => import('./pages/unidades/unidades-page').then((m) => m.UnidadesPage),
      },
      {
        path: 'consultorios',
        canActivate: [authorizationGuard(['consultorio', 'read'])],
        loadComponent: () =>
          import('./pages/consultorios/consultorios-page').then((m) => m.ConsultoriosPage),
      },
      {
        path: 'usuarios',
        canActivate: [authorizationGuard(['usuario', 'read'])],
        loadComponent: () => import('./pages/usuarios/usuarios-page').then((m) => m.UsuariosPage),
      },
      {
        path: 'medicos',
        canActivate: [authorizationGuard(['medico', 'read'])],
        loadComponent: () => import('./pages/medicos/medicos-page').then((m) => m.MedicosPage),
      },
      {
        path: 'especialidades',
        canActivate: [authorizationGuard(['especialidade', 'read'])],
        loadComponent: () =>
          import('./pages/especialidades/especialidades-page').then((m) => m.EspecialidadesPage),
      },
      {
        path: 'alocacoes-medicas',
        canActivate: [authorizationGuard(['alocacao-medico', 'read'])],
        loadComponent: () =>
          import('./pages/alocacoes-medicas/alocacoes-medicas-page').then(
            (m) => m.AlocacoesMedicasPage,
          ),
      },
      {
        path: 'agendas-medicas',
        canActivate: [authorizationGuard(['agenda-medica', 'read'])],
        loadComponent: () =>
          import('./pages/agendas-medicas/agendas-medicas-page').then((m) => m.AgendasMedicasPage),
      },
      {
        path: 'bloqueios-agenda',
        canActivate: [authorizationGuard(['bloqueio-agenda', 'read'])],
        loadComponent: () =>
          import('./pages/bloqueios-agenda/bloqueios-agenda-page').then(
            (m) => m.BloqueiosAgendaPage,
          ),
      },
      {
        path: 'consultas',
        canActivate: [authorizationGuard(['consulta', 'read'])],
        loadComponent: () =>
          import('./pages/consultas/consultas-page').then((m) => m.ConsultasPage),
      },
      {
        path: 'registros-atendimento',
        canActivate: [authorizationGuard(['registro-atendimento', 'read'])],
        loadComponent: () =>
          import('./pages/registros-atendimento/registros-atendimento-page').then(
            (m) => m.RegistrosAtendimentoPage,
          ),
      },
      {
        path: 'anexos-consulta',
        canActivate: [authorizationGuard(['anexo-consulta', 'read'])],
        loadComponent: () =>
          import('./pages/anexos-consulta/anexos-consulta-page').then((m) => m.AnexosConsultaPage),
      },
    ],
  },
];
