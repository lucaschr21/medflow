import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { orDash, shortId } from '../../@shared/resource/resource-formatters';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Unidade } from '../../schemas/unidade.schema';
import { UnidadeService } from '../../services/unidade.service';

@Component({
  selector: 'app-unidades-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Unidades"
      subtitle="Cadastro das unidades vinculadas às organizações."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma unidade encontrada."
    />
  `,
})
export class UnidadesPage extends ResourceListPageBase<Unidade> {
  protected readonly service = inject(UnidadeService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'cidade', header: 'Cidade' },
    { field: 'organizacaoId', header: 'Organização' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['unidade', 'create']) ? 'Nova unidade' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((unidade) => ({
      id: unidade.id,
      values: {
        nome: unidade.nome,
        telefone: unidade.telefone,
        cidade: `${orDash(unidade.endereco.cidade)} / ${orDash(unidade.endereco.uf)}`,
        organizacaoId: shortId(unidade.organizacaoId),
      },
    })),
  );
}
