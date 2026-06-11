import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { joinValues, shortId } from '../../@shared/resource/resource-formatters';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
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
      subtitle="Médicos cadastrados. Crie um usuário com papel Médico para adicionar."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum médico encontrado."
      (createClick)="router.navigate(['/usuarios/novo'])"
    />
  `,
})
export class MedicosPage extends ResourceListPageBase<Medico> {
  protected readonly service = inject(MedicoService);
  readonly router = inject(Router);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'usuarioId', header: 'Usuário' },
    { field: 'especialidadeIds', header: 'Especialidades' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['usuario', 'create']) ? 'Novo médico (criar usuário)' : null,
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
