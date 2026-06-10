import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Organizacao, OrganizacaoInput } from '../../schemas/organizacao.schema';
import { OrganizacaoService } from '../../services/organizacao.service';

@Component({
  selector: 'app-organizacoes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel],
  template: `
    <app-resource-table-page
      title="Organizações"
      subtitle="Gestão das organizações cadastradas no Medflow."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma organização encontrada."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      width="560px"
      (visibleChange)="dialogVisible.set($event)"
      (confirm)="saveDialog()"
      (cancel)="closeDialog()"
    >
      <div class="flex flex-col gap-5 pt-2">
        <p-floatlabel variant="on">
          <input pInputText id="nome" [(ngModel)]="formNome" style="width:100%" />
          <label for="nome">Nome *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="email" type="email" [(ngModel)]="formEmail" style="width:100%" />
          <label for="email">E-mail *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="telefone" [(ngModel)]="formTelefone" style="width:100%" />
          <label for="telefone">Telefone</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="corPrimaria" type="color" [(ngModel)]="formCorPrimaria" style="width:100%;height:48px;padding:4px 8px" />
          <label for="corPrimaria">Cor primária</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class OrganizacoesPage extends ResourceCrudPageBase<Organizacao, OrganizacaoInput> {
  protected readonly service = inject(OrganizacaoService);
  private readonly authorizationService = inject(AuthorizationService);

  formNome = signal('');
  formEmail = signal('');
  formTelefone = signal('');
  formCorPrimaria = signal('#2563eb');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'email', header: 'E-mail' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'corPrimaria', header: 'Cor primária' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['organizacao', 'create']) ? 'Nova organização' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['organizacao', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((o) => ({
      id: o.id,
      values: { nome: o.nome, email: o.email, telefone: o.telefone, corPrimaria: o.corPrimaria },
    })),
  );

  protected createTitle() { return 'Nova organização'; }
  protected editTitle() { return 'Editar organização'; }

  buildCreateInput(): OrganizacaoInput {
    return {
      nome: this.formNome(),
      email: this.formEmail(),
      telefone: this.formTelefone(),
      corPrimaria: this.formCorPrimaria(),
      logotipo: null,
      logotipoContentType: null,
    };
  }
  buildEditInput(): OrganizacaoInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((o) => o.id === id);
    if (!e) return;
    this.formNome.set(e.nome);
    this.formEmail.set(e.email);
    this.formTelefone.set(e.telefone);
    this.formCorPrimaria.set(e.corPrimaria);
  }

  override openCreateDialog(): void {
    this.formNome.set('');
    this.formEmail.set('');
    this.formTelefone.set('');
    this.formCorPrimaria.set('#2563eb');
    super.openCreateDialog();
  }
}
