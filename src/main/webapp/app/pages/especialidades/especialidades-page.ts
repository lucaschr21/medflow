import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { FloatLabel } from 'primeng/floatlabel';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { orDash } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Especialidade, EspecialidadeInput } from '../../schemas/especialidade.schema';
import { EspecialidadeService } from '../../services/especialidade.service';

@Component({
  selector: 'app-especialidades-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, TextareaModule, FloatLabel],
  template: `
    <app-resource-table-page
      title="Especialidades"
      subtitle="Lista das especialidades médicas cadastradas."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma especialidade encontrada."
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
          <input pInputText id="nome" [(ngModel)]="formNome" [required]="true" style="width:100%" />
          <label for="nome">Nome *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <textarea pTextarea id="descricao" [(ngModel)]="formDescricao" rows="3" style="width:100%"></textarea>
          <label for="descricao">Descrição</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class EspecialidadesPage extends ResourceCrudPageBase<Especialidade, EspecialidadeInput> {
  protected readonly service = inject(EspecialidadeService);
  private readonly authorizationService = inject(AuthorizationService);

  formNome = signal('');
  formDescricao = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'descricao', header: 'Descrição' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['especialidade', 'create']) ? 'Nova especialidade' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['especialidade', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((e) => ({
      id: e.id,
      values: { nome: e.nome, descricao: orDash(e.descricao) },
    })),
  );

  protected createTitle() { return 'Nova especialidade'; }
  protected editTitle() { return 'Editar especialidade'; }

  buildCreateInput(): EspecialidadeInput {
    return { nome: this.formNome(), descricao: this.formDescricao() || null };
  }

  buildEditInput(): EspecialidadeInput {
    return this.buildCreateInput();
  }

  onEditOpen(id: string): void {
    const entity = this.entities().find((e) => e.id === id);
    if (!entity) return;
    this.formNome.set(entity.nome);
    this.formDescricao.set(entity.descricao ?? '');
  }

  override openCreateDialog(): void {
    this.formNome.set('');
    this.formDescricao.set('');
    super.openCreateDialog();
  }
}
