import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { formatDateTime, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { AlocacaoMedico } from '../../schemas/alocacao-medico.schema';
import { AlocacaoMedicoService } from '../../services/alocacao-medico.service';

@Component({
  selector: 'app-alocacoes-medicas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Alocações médicas"
      subtitle="Distribuição de médicos por consultório e período."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma alocação encontrada."
    />
  `,
})
export class AlocacoesMedicasPage extends ResourceListPageBase<AlocacaoMedico> {
  protected readonly service = inject(AlocacaoMedicoService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'medicoId', header: 'Médico' },
    { field: 'consultorioId', header: 'Consultório' },
    { field: 'dataInicio', header: 'Início' },
    { field: 'dataFim', header: 'Fim' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['alocacao-medico', 'create']) ? 'Novo registro' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((alocacao) => ({
      id: alocacao.id,
      values: {
        medicoId: shortId(alocacao.medicoId),
        consultorioId: shortId(alocacao.consultorioId),
        dataInicio: formatDateTime(alocacao.dataInicio),
        dataFim: formatDateTime(alocacao.dataFim),
      },
    })),
  );
}
