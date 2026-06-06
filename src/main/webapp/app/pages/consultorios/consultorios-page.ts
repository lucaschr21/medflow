import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Consultorio } from '../../schemas/consultorio.schema';
import { ConsultorioService } from '../../services/consultorio.service';

@Component({
  selector: 'app-consultorios-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Consultórios"
      subtitle="Gestão dos consultórios e salas disponíveis."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum consultório encontrado."
    />
  `,
})
export class ConsultoriosPage extends ResourceListPageBase<Consultorio> {
  protected readonly service = inject(ConsultorioService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'sala', header: 'Sala' },
    { field: 'unidadeId', header: 'Unidade' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['consultorio', 'create']) ? 'Novo consultório' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((consultorio) => ({
      id: consultorio.id,
      values: {
        nome: consultorio.nome,
        sala: consultorio.sala,
        unidadeId: shortId(consultorio.unidadeId),
      },
    })),
  );
}
