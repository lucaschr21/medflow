import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { orDash, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Usuario } from '../../schemas/usuario.schema';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-usuarios-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Usuários"
      subtitle="Gestão de usuários vinculados ao Keycloak e à organização."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum usuário encontrado."
    />
  `,
})
export class UsuariosPage extends ResourceListPageBase<Usuario> {
  protected readonly service = inject(UsuarioService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'keycloakId', header: 'Keycloak ID' },
    { field: 'organizacaoId', header: 'Organização' },
    { field: 'medicoId', header: 'Médico vinculado' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['usuario', 'create']) ? 'Novo usuário' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((usuario) => ({
      id: usuario.id,
      values: {
        keycloakId: shortId(usuario.keycloakId),
        organizacaoId: shortId(usuario.organizacaoId),
        medicoId: orDash(shortId(usuario.medicoId)),
      },
    })),
  );
}
