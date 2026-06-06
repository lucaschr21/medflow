import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { formatDateTime, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { BloqueioAgenda } from '../../schemas/bloqueio-agenda.schema';
import { BloqueioAgendaService } from '../../services/bloqueio-agenda.service';

@Component({
  selector: 'app-bloqueios-agenda-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Bloqueios de agenda"
      subtitle="Períodos indisponíveis aplicados à agenda operacional."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum bloqueio encontrado."
    />
  `,
})
export class BloqueiosAgendaPage extends ResourceListPageBase<BloqueioAgenda> {
  protected readonly service = inject(BloqueioAgendaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'tipo', header: 'Tipo' },
    { field: 'inicio', header: 'Início' },
    { field: 'fim', header: 'Fim' },
    { field: 'medicoId', header: 'Médico' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['bloqueio-agenda', 'create']) ? 'Novo bloqueio' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((bloqueio) => ({
      id: bloqueio.id,
      values: {
        tipo: bloqueio.tipo,
        inicio: formatDateTime(bloqueio.inicio),
        fim: formatDateTime(bloqueio.fim),
        medicoId: shortId(bloqueio.medicoId),
      },
    })),
  );
}
