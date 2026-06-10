import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';
import { TableModule } from 'primeng/table';
import { map } from 'rxjs';

import { PageHeader } from '../../@shared/layout/page-header/page-header';
import { formatDateTime } from '../../@shared/resource/resource-formatters';
import { ConsultaService } from '../../services/consulta.service';
import type { StatusConsulta } from '../../schemas/enums/status-consulta.enum';

const STATUS_LABEL: Record<StatusConsulta, string> = {
  AGENDADA: 'Agendada',
  CONFIRMADA: 'Confirmada',
  EM_ESPERA: 'Em espera',
  EM_ATENDIMENTO: 'Em atendimento',
  FINALIZADA: 'Finalizada',
  CANCELADA: 'Cancelada',
  NAO_COMPARECEU: 'Não compareceu',
};

const STATUS_SEVERITY: Record<StatusConsulta, string> = {
  AGENDADA: 'info',
  CONFIRMADA: 'success',
  EM_ESPERA: 'warn',
  EM_ATENDIMENTO: 'warn',
  FINALIZADA: 'secondary',
  CANCELADA: 'danger',
  NAO_COMPARECEU: 'danger',
};

@Component({
  selector: 'app-dashboard-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [PageHeader, Card, Tag, TableModule, RouterLink],
  template: `
    <app-page-header title="Painel" subtitle="Visão geral do sistema Medflow." />

    <div class="grid grid-cols-2 gap-4 mb-6 sm:grid-cols-4">
      @for (stat of stats(); track stat.label) {
        <p-card styleClass="text-center">
          <p class="text-3xl font-semibold text-primary mb-1">{{ stat.value }}</p>
          <p class="text-sm text-surface-500">{{ stat.label }}</p>
        </p-card>
      }
    </div>

    <p-card header="Consultas recentes">
      @if (loadingConsultas()) {
        <p class="text-surface-400">Carregando...</p>
      } @else if (consultasRecentes().length) {
        <p-table [value]="consultasRecentes()" size="small" [stripedRows]="true">
          <ng-template #header>
            <tr>
              <th>Status</th>
              <th>Início</th>
              <th>Tipo</th>
              <th>Motivo</th>
            </tr>
          </ng-template>
          <ng-template #body let-row>
            <tr>
              <td>
                <p-tag [value]="row.statusLabel" [severity]="row.severity" />
              </td>
              <td>{{ row.inicio }}</td>
              <td>{{ row.tipo }}</td>
              <td>{{ row.motivo }}</td>
            </tr>
          </ng-template>
        </p-table>
        <div class="mt-3 text-right">
          <a routerLink="/consultas" class="text-sm text-primary hover:underline">Ver todas as consultas →</a>
        </div>
      } @else {
        <p class="text-surface-400">Nenhuma consulta encontrada.</p>
      }
    </p-card>
  `,
})
export class DashboardPage {
  private readonly consultaService = inject(ConsultaService);

  private readonly consultasResource = rxResource({
    stream: () => this.consultaService.findAll({ page: 0, size: 10 }),
  });

  readonly loadingConsultas = this.consultasResource.isLoading;

  readonly consultasRecentes = computed(() => {
    const content = this.consultasResource.value()?.content ?? [];
    return content.map((c) => ({
      statusLabel: STATUS_LABEL[c.status] ?? c.status,
      severity: STATUS_SEVERITY[c.status] ?? 'secondary',
      inicio: formatDateTime(c.dataHoraInicio),
      tipo: c.tipoConsulta,
      motivo: c.motivo,
    }));
  });

  readonly stats = computed(() => {
    const consultas = this.consultasResource.value()?.content ?? [];
    const total = this.consultasResource.value()?.totalElements ?? 0;
    const agendadas = consultas.filter((c) => c.status === 'AGENDADA').length;
    const emAtendimento = consultas.filter((c) => c.status === 'EM_ATENDIMENTO').length;
    const finalizadas = consultas.filter((c) => c.status === 'FINALIZADA').length;
    return [
      { label: 'Total de consultas', value: total },
      { label: 'Agendadas', value: agendadas },
      { label: 'Em atendimento', value: emAtendimento },
      { label: 'Finalizadas', value: finalizadas },
    ];
  });
}
