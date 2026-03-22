import type { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./layouts/app-layout/app-layout.component').then((m) => m.AppLayoutComponent),
    children: [
      {
        path: 'teleconsulta',
        loadComponent: () =>
          import('./pages/teleconsulta/dashboard-teleconsulta.component').then(
            (m) => m.DashboardTeleconsultaComponent,
          ),
      },
      {
        path: '',
        redirectTo: 'teleconsulta',
        pathMatch: 'full',
      },
    ],
  },
];
