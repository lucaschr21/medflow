import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { joinValues, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Medico } from '../../schemas/medico.schema';
import { MedicoService } from '../../services/medico.service';

@Component({
  selector: 'app-medicos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Médicos"
      subtitle="Cadastro e vínculo dos médicos com usuários e especialidades."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum médico encontrado."
    />
  `,
})
export class MedicosPage extends ResourceListPageBase<Medico> {
  protected readonly service = inject(MedicoService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'usuarioId', header: 'Usuário' },
    { field: 'especialidadeIds', header: 'Especialidades' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['medico', 'create']) ? 'Novo médico' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((medico) => ({
      id: medico.id,
      values: {
        usuarioId: shortId(medico.usuarioId),
        especialidadeIds: joinValues(medico.especialidadeIds),
      },
    })),
  );
}
