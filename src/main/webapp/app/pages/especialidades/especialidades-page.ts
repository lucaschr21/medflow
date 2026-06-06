import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { orDash } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Especialidade } from '../../schemas/especialidade.schema';
import { EspecialidadeService } from '../../services/especialidade.service';

@Component({
  selector: 'app-especialidades-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Especialidades"
      subtitle="Lista das especialidades médicas cadastradas."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma especialidade encontrada."
    />
  `,
})
export class EspecialidadesPage extends ResourceListPageBase<Especialidade> {
  protected readonly service = inject(EspecialidadeService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'descricao', header: 'Descrição' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['especialidade', 'create']) ? 'Nova especialidade' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((especialidade) => ({
      id: especialidade.id,
      values: {
        nome: especialidade.nome,
        descricao: orDash(especialidade.descricao),
      },
    })),
  );
}
