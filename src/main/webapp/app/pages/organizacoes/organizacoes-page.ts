import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import { OrganizacaoService } from '../../services/organizacao.service';
import type { Organizacao } from '../../schemas/organizacao.schema';

@Component({
  selector: 'app-organizacoes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Organizações"
      subtitle="Gestão das organizações cadastradas no Medflow."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma organização encontrada."
    />
  `,
})
export class OrganizacoesPage extends ResourceListPageBase<Organizacao> {
  protected readonly service = inject(OrganizacaoService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'email', header: 'E-mail' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'corPrimaria', header: 'Cor primária' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['organizacao', 'create']) ? 'Novo registro' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((organizacao) => ({
      id: organizacao.id,
      values: {
        nome: organizacao.nome,
        email: organizacao.email,
        telefone: organizacao.telefone,
        corPrimaria: organizacao.corPrimaria,
      },
    })),
  );
}
