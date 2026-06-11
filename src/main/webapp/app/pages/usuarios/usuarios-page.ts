import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { orDash } from '../../@shared/resource/resource-formatters';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
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
      subtitle="Gestão de usuários do sistema."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum usuário encontrado."
      (createClick)="router.navigate(['/usuarios/novo'])"
    />
  `,
})
export class UsuariosPage extends ResourceListPageBase<Usuario> {
  protected readonly service = inject(UsuarioService);
  readonly router = inject(Router);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'keycloakId', header: 'ID Keycloak' },
    { field: 'organizacaoId', header: 'Organização' },
    { field: 'medicoId', header: 'Médico' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['usuario', 'create']) ? 'Novo usuário' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((u) => ({
      id: u.id,
      values: {
        keycloakId: u.keycloakId.substring(0, 8) + '...',
        organizacaoId: u.organizacaoId.substring(0, 8) + '...',
        medicoId: orDash(u.medicoId?.substring(0, 8) + '...'),
      },
    })),
  );
}
