import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';
import { DatePickerModule } from 'primeng/datepicker';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { formatDateTime, orDash, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { AlocacaoMedico, AlocacaoMedicoInput } from '../../schemas/alocacao-medico.schema';
import { AlocacaoMedicoService } from '../../services/alocacao-medico.service';

@Component({
  selector: 'app-alocacoes-medicas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel, DatePickerModule],
  template: `
    <app-resource-table-page
      title="Alocações médicas"
      subtitle="Vínculo de médicos com consultórios em períodos específicos."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma alocação encontrada."
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
          <input pInputText id="medicoId" [(ngModel)]="formMedicoId" style="width:100%" />
          <label for="medicoId">ID do Médico *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="consultorioId" [(ngModel)]="formConsultorioId" style="width:100%" />
          <label for="consultorioId">ID do Consultório *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <p-datepicker id="dataInicio" [(ngModel)]="formDataInicio" dateFormat="dd/mm/yy" style="width:100%" />
          <label for="dataInicio">Data de início *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <p-datepicker id="dataFim" [(ngModel)]="formDataFim" dateFormat="dd/mm/yy" style="width:100%" />
          <label for="dataFim">Data de fim (opcional)</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class AlocacoesMedicasPage extends ResourceCrudPageBase<AlocacaoMedico, AlocacaoMedicoInput> {
  protected readonly service = inject(AlocacaoMedicoService);
  private readonly authorizationService = inject(AuthorizationService);

  formMedicoId = signal('');
  formConsultorioId = signal('');
  formDataInicio = signal<Date | null>(null);
  formDataFim = signal<Date | null>(null);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'medicoId', header: 'Médico' },
    { field: 'consultorioId', header: 'Consultório' },
    { field: 'dataInicio', header: 'Início' },
    { field: 'dataFim', header: 'Fim' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['alocacao-medico', 'create']) ? 'Nova alocação' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['alocacao-medico', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((a) => ({
      id: a.id,
      values: {
        medicoId: shortId(a.medicoId),
        consultorioId: shortId(a.consultorioId),
        dataInicio: formatDateTime(a.dataInicio),
        dataFim: orDash(formatDateTime(a.dataFim)),
      },
    })),
  );

  protected createTitle() { return 'Nova alocação médica'; }
  protected editTitle() { return 'Editar alocação médica'; }

  buildCreateInput(): AlocacaoMedicoInput {
    return {
      medicoId: this.formMedicoId(),
      consultorioId: this.formConsultorioId(),
      dataInicio: this.formDataInicio()?.toISOString() ?? '',
      dataFim: this.formDataFim()?.toISOString() ?? null,
    };
  }
  buildEditInput(): AlocacaoMedicoInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((a) => a.id === id);
    if (!e) return;
    this.formMedicoId.set(e.medicoId);
    this.formConsultorioId.set(e.consultorioId);
    this.formDataInicio.set(e.dataInicio ? new Date(e.dataInicio) : null);
    this.formDataFim.set(e.dataFim ? new Date(e.dataFim) : null);
  }

  override openCreateDialog(): void {
    this.formMedicoId.set(''); this.formConsultorioId.set('');
    this.formDataInicio.set(null); this.formDataFim.set(null);
    super.openCreateDialog();
  }
}
