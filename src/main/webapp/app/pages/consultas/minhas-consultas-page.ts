import { DatePipe } from '@angular/common';
import { httpResource } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonDirective } from 'primeng/button';
import { Skeleton } from 'primeng/skeleton';
import { Tab, TabList, TabPanel, TabPanels, Tabs } from 'primeng/tabs';
import { Tag } from 'primeng/tag';

import type { PageResult } from '../../@core/persistence/page-result';
import { environment } from '../../environments/environment';
import type { Consulta } from '../../schemas/consulta.schema';

@Component({
  selector: 'app-minhas-consultas-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    RouterLink,
    DatePipe,
    ButtonDirective,
    Tabs,
    TabList,
    Tab,
    TabPanels,
    TabPanel,
    Tag,
    Skeleton,
  ],
  templateUrl: './minhas-consultas-page.html',
})
export class MinhasConsultasPage {
  readonly activeTab = signal('0');

  readonly consultasResource = httpResource<PageResult<Consulta>>(
    () => `${environment.api.baseUrl}/consultas?page=0&size=50`,
  );

  statusLabel(status: string): string {
    const labels: Record<string, string> = {
      AGENDADA: 'Agendada',
      CONFIRMADA: 'Confirmada',
      EM_ESPERA: 'Em espera',
      EM_ATENDIMENTO: 'Em atendimento',
      FINALIZADA: 'Finalizada',
      CANCELADA: 'Cancelada',
      NAO_COMPARECEU: 'Não compareceu',
    };
    return labels[status] ?? status;
  }

  statusSeverity(status: string): 'info' | 'success' | 'warn' | 'danger' | 'secondary' {
    const map: Record<string, 'info' | 'success' | 'warn' | 'danger' | 'secondary'> = {
      AGENDADA: 'info',
      CONFIRMADA: 'info',
      EM_ESPERA: 'warn',
      EM_ATENDIMENTO: 'success',
      FINALIZADA: 'secondary',
      CANCELADA: 'danger',
      NAO_COMPARECEU: 'danger',
    };
    return map[status] ?? 'info';
  }
}
