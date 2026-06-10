import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
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
import type { RegistroAtendimento, RegistroAtendimentoInput } from '../../schemas/registro-atendimento.schema';
import { RegistroAtendimentoService } from '../../services/registro-atendimento.service';

@Component({
  selector: 'app-registros-atendimento-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, TextareaModule, FloatLabel],
  template: `
    <app-resource-table-page
      title="Registros de atendimento"
      subtitle="Prontuário eletrônico — anamnese, conduta e observações."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum registro encontrado."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      width="640px"
      (visibleChange)="dialogVisible.set($event)"
      (confirm)="saveDialog()"
      (cancel)="closeDialog()"
    >
      <div class="flex flex-col gap-5 pt-2">
        <p-floatlabel variant="on">
          <input pInputText id="consultaId" [(ngModel)]="formConsultaId" style="width:100%" />
          <label for="consultaId">ID da Consulta *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="medicoId" [(ngModel)]="formMedicoId" style="width:100%" />
          <label for="medicoId">ID do Médico *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="queixaPrincipal" [(ngModel)]="formQueixaPrincipal" style="width:100%" />
          <label for="queixaPrincipal">Queixa principal *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <textarea pTextarea id="anamnese" [(ngModel)]="formAnamnese" rows="4" style="width:100%"></textarea>
          <label for="anamnese">Anamnese *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <textarea pTextarea id="conduta" [(ngModel)]="formConduta" rows="4" style="width:100%"></textarea>
          <label for="conduta">Conduta *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <textarea pTextarea id="observacoes" [(ngModel)]="formObservacoes" rows="3" style="width:100%"></textarea>
          <label for="observacoes">Observações</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class RegistrosAtendimentoPage extends ResourceCrudPageBase<RegistroAtendimento, RegistroAtendimentoInput> {
  protected readonly service = inject(RegistroAtendimentoService);
  private readonly authorizationService = inject(AuthorizationService);

  formConsultaId = signal('');
  formMedicoId = signal('');
  formQueixaPrincipal = signal('');
  formAnamnese = signal('');
  formConduta = signal('');
  formObservacoes = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'consultaId', header: 'Consulta' },
    { field: 'medicoId', header: 'Médico' },
    { field: 'queixaPrincipal', header: 'Queixa principal' },
    { field: 'conduta', header: 'Conduta' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['registro-atendimento', 'create']) ? 'Novo registro' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['registro-atendimento', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((r) => ({
      id: r.id,
      values: {
        consultaId: shortId(r.consultaId),
        medicoId: shortId(r.medicoId),
        queixaPrincipal: r.queixaPrincipal,
        conduta: r.conduta.length > 80 ? r.conduta.slice(0, 80) + '...' : r.conduta,
      },
    })),
  );

  protected createTitle() { return 'Novo registro de atendimento'; }
  protected editTitle() { return 'Editar registro de atendimento'; }

  buildCreateInput(): RegistroAtendimentoInput {
    return {
      consultaId: this.formConsultaId(),
      medicoId: this.formMedicoId(),
      queixaPrincipal: this.formQueixaPrincipal(),
      anamnese: this.formAnamnese(),
      conduta: this.formConduta(),
      observacoes: this.formObservacoes() || null,
    };
  }
  buildEditInput(): RegistroAtendimentoInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((r) => r.id === id);
    if (!e) return;
    this.formConsultaId.set(e.consultaId);
    this.formMedicoId.set(e.medicoId);
    this.formQueixaPrincipal.set(e.queixaPrincipal);
    this.formAnamnese.set(e.anamnese);
    this.formConduta.set(e.conduta);
    this.formObservacoes.set(e.observacoes ?? '');
  }

  override openCreateDialog(): void {
    this.formConsultaId.set(''); this.formMedicoId.set(''); this.formQueixaPrincipal.set('');
    this.formAnamnese.set(''); this.formConduta.set(''); this.formObservacoes.set('');
    super.openCreateDialog();
  }
}
