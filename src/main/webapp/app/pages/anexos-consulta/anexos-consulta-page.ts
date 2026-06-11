import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { formatBytes, orDash, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { AnexoConsulta } from '../../schemas/anexo-consulta.schema';
import { AnexoConsultaService } from '../../services/anexo-consulta.service';

@Component({
  selector: 'app-anexos-consulta-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Anexos de consulta"
      subtitle="Arquivos vinculados às consultas e atendimentos."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum anexo encontrado."
    />
  `,
})
export class AnexosConsultaPage extends ResourceListPageBase<AnexoConsulta> {
  protected readonly service = inject(AnexoConsultaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nomeArquivo', header: 'Arquivo' },
    { field: 'contentType', header: 'Tipo' },
    { field: 'tamanhoBytes', header: 'Tamanho' },
    { field: 'consultaId', header: 'Consulta' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['anexo-consulta', 'create']) ? 'Novo registro' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((anexo) => ({
      id: anexo.id,
      values: {
        nomeArquivo: anexo.nomeArquivo,
        contentType: orDash(anexo.contentType),
        tamanhoBytes: formatBytes(anexo.tamanhoBytes),
        consultaId: shortId(anexo.consultaId),
      },
    })),
  );
}
