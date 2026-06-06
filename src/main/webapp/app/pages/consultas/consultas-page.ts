import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { formatDateTime } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Consulta } from '../../schemas/consulta.schema';
import { ConsultaService } from '../../services/consulta.service';

@Component({
  selector: 'app-consultas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Consultas"
      subtitle="Agenda consolidada das consultas registradas."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma consulta encontrada."
    />
  `,
})
export class ConsultasPage extends ResourceListPageBase<Consulta> {
  protected readonly service = inject(ConsultaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'status', header: 'Status' },
    { field: 'dataHoraInicio', header: 'Início' },
    { field: 'dataHoraFim', header: 'Fim' },
    { field: 'motivo', header: 'Motivo' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['consulta', 'create']) ? 'Nova consulta' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((consulta) => ({
      id: consulta.id,
      values: {
        status: consulta.status,
        dataHoraInicio: formatDateTime(consulta.dataHoraInicio),
        dataHoraFim: formatDateTime(consulta.dataHoraFim),
        motivo: consulta.motivo,
      },
    })),
  );
}
