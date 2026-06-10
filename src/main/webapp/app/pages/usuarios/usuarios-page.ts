import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { orDash, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Usuario, UsuarioInput } from '../../schemas/usuario.schema';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-usuarios-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel],
  template: `
    <app-resource-table-page
      title="Usuários"
      subtitle="Gestão de usuários vinculados ao Keycloak e à organização."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum usuário encontrado."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      (visibleChange)="dialogVisible.set($event)"
      (confirm)="saveDialog()"
      (cancel)="closeDialog()"
    >
      <div class="flex flex-col gap-5 pt-2">
        <p-floatlabel variant="on">
          <input pInputText id="keycloakId" [(ngModel)]="formKeycloakId" style="width:100%" />
          <label for="keycloakId">Keycloak ID *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="organizacaoId" [(ngModel)]="formOrganizacaoId" style="width:100%" />
          <label for="organizacaoId">ID da Organização *</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class UsuariosPage extends ResourceCrudPageBase<Usuario, UsuarioInput> {
  protected readonly service = inject(UsuarioService);
  private readonly authorizationService = inject(AuthorizationService);

  formKeycloakId = signal('');
  formOrganizacaoId = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'keycloakId', header: 'Keycloak ID' },
    { field: 'organizacaoId', header: 'Organização' },
    { field: 'medicoId', header: 'Médico vinculado' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['usuario', 'create']) ? 'Novo usuário' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['usuario', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((u) => ({
      id: u.id,
      values: {
        keycloakId: shortId(u.keycloakId),
        organizacaoId: shortId(u.organizacaoId),
        medicoId: orDash(shortId(u.medicoId)),
      },
    })),
  );

  protected createTitle() { return 'Novo usuário'; }
  protected editTitle() { return 'Editar usuário'; }

  buildCreateInput(): UsuarioInput {
    return { keycloakId: this.formKeycloakId(), organizacaoId: this.formOrganizacaoId() };
  }
  buildEditInput(): UsuarioInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((u) => u.id === id);
    if (!e) return;
    this.formKeycloakId.set(e.keycloakId);
    this.formOrganizacaoId.set(e.organizacaoId);
  }

  override openCreateDialog(): void {
    this.formKeycloakId.set(''); this.formOrganizacaoId.set('');
    super.openCreateDialog();
  }
}
