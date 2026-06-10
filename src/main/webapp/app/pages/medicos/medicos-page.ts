import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { joinValues, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Medico, MedicoInput } from '../../schemas/medico.schema';
import { MedicoService } from '../../services/medico.service';

@Component({
  selector: 'app-medicos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel],
  template: `
    <app-resource-table-page
      title="Médicos"
      subtitle="Cadastro e vínculo dos médicos com usuários e especialidades."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum médico encontrado."
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
          <input pInputText id="usuarioId" [(ngModel)]="formUsuarioId" style="width:100%" />
          <label for="usuarioId">ID do Usuário *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="especialidadeIds" [(ngModel)]="formEspecialidadeIds" style="width:100%" />
          <label for="especialidadeIds">IDs de Especialidades (separados por vírgula)</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class MedicosPage extends ResourceCrudPageBase<Medico, MedicoInput> {
  protected readonly service = inject(MedicoService);
  private readonly authorizationService = inject(AuthorizationService);

  formUsuarioId = signal('');
  formEspecialidadeIds = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'usuarioId', header: 'Usuário' },
    { field: 'especialidadeIds', header: 'Especialidades' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['medico', 'create']) ? 'Novo médico' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['medico', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((m) => ({
      id: m.id,
      values: {
        usuarioId: shortId(m.usuarioId),
        especialidadeIds: joinValues(m.especialidadeIds),
      },
    })),
  );

  protected createTitle() { return 'Novo médico'; }
  protected editTitle() { return 'Editar médico'; }

  buildCreateInput(): MedicoInput {
    const ids = this.formEspecialidadeIds()
      .split(',')
      .map((s) => s.trim())
      .filter(Boolean);
    return { usuarioId: this.formUsuarioId(), especialidadeIds: ids };
  }
  buildEditInput(): MedicoInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((m) => m.id === id);
    if (!e) return;
    this.formUsuarioId.set(e.usuarioId);
    this.formEspecialidadeIds.set(e.especialidadeIds.join(', '));
  }

  override openCreateDialog(): void {
    this.formUsuarioId.set(''); this.formEspecialidadeIds.set('');
    super.openCreateDialog();
  }
}
