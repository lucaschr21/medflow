import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Consultorio, ConsultorioInput } from '../../schemas/consultorio.schema';
import { ConsultorioService } from '../../services/consultorio.service';

@Component({
  selector: 'app-consultorios-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel],
  template: `
    <app-resource-table-page
      title="Consultórios"
      subtitle="Gestão dos consultórios e salas disponíveis."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum consultório encontrado."
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
          <input pInputText id="nome" [(ngModel)]="formNome" style="width:100%" />
          <label for="nome">Nome *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="sala" [(ngModel)]="formSala" style="width:100%" />
          <label for="sala">Sala *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="unidadeId" [(ngModel)]="formUnidadeId" style="width:100%" />
          <label for="unidadeId">ID da Unidade *</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class ConsultoriosPage extends ResourceCrudPageBase<Consultorio, ConsultorioInput> {
  protected readonly service = inject(ConsultorioService);
  private readonly authorizationService = inject(AuthorizationService);

  formNome = signal('');
  formSala = signal('');
  formUnidadeId = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'sala', header: 'Sala' },
    { field: 'unidadeId', header: 'Unidade' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['consultorio', 'create']) ? 'Novo consultório' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['consultorio', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((c) => ({
      id: c.id,
      values: { nome: c.nome, sala: c.sala, unidadeId: shortId(c.unidadeId) },
    })),
  );

  protected createTitle() { return 'Novo consultório'; }
  protected editTitle() { return 'Editar consultório'; }

  buildCreateInput(): ConsultorioInput {
    return { nome: this.formNome(), sala: this.formSala(), unidadeId: this.formUnidadeId() };
  }
  buildEditInput(): ConsultorioInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((c) => c.id === id);
    if (!e) return;
    this.formNome.set(e.nome);
    this.formSala.set(e.sala);
    this.formUnidadeId.set(e.unidadeId);
  }

  override openCreateDialog(): void {
    this.formNome.set(''); this.formSala.set(''); this.formUnidadeId.set('');
    super.openCreateDialog();
  }
}
