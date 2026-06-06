import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { RegistroAtendimento } from '../../schemas/registro-atendimento.schema';
import { RegistroAtendimentoService } from '../../services/registro-atendimento.service';

@Component({
  selector: 'app-registros-atendimento-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Registros de atendimento"
      subtitle="Evolução clínica registrada durante os atendimentos."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum registro de atendimento encontrado."
    />
  `,
})
export class RegistrosAtendimentoPage extends ResourceListPageBase<RegistroAtendimento> {
  protected readonly service = inject(RegistroAtendimentoService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'consultaId', header: 'Consulta' },
    { field: 'medicoId', header: 'Médico' },
    { field: 'queixaPrincipal', header: 'Queixa principal' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['registro-atendimento', 'create']) ? 'Novo registro' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((registro) => ({
      id: registro.id,
      values: {
        consultaId: shortId(registro.consultaId),
        medicoId: shortId(registro.medicoId),
        queixaPrincipal: registro.queixaPrincipal,
      },
    })),
  );
}
