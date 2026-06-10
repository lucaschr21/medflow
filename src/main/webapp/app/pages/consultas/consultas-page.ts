import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';


import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { formatDateTime, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Consulta, ConsultaInput } from '../../schemas/consulta.schema';
import type { StatusConsulta } from '../../schemas/enums/status-consulta.enum';
import { ConsultaService } from '../../services/consulta.service';

const STATUS_OPTIONS: { label: string; value: StatusConsulta }[] = [
  { label: 'Agendada', value: 'AGENDADA' },
  { label: 'Confirmada', value: 'CONFIRMADA' },
  { label: 'Em espera', value: 'EM_ESPERA' },
  { label: 'Em atendimento', value: 'EM_ATENDIMENTO' },
  { label: 'Finalizada', value: 'FINALIZADA' },
  { label: 'Cancelada', value: 'CANCELADA' },
  { label: 'Não compareceu', value: 'NAO_COMPARECEU' },
];

const STATUS_SEVERITY: Record<StatusConsulta, string> = {
  AGENDADA: 'info',
  CONFIRMADA: 'success',
  EM_ESPERA: 'warn',
  EM_ATENDIMENTO: 'warn',
  FINALIZADA: 'secondary',
  CANCELADA: 'danger',
  NAO_COMPARECEU: 'danger',
};

const STATUS_LABEL: Record<StatusConsulta, string> = {
  AGENDADA: 'Agendada',
  CONFIRMADA: 'Confirmada',
  EM_ESPERA: 'Em espera',
  EM_ATENDIMENTO: 'Em atendimento',
  FINALIZADA: 'Finalizada',
  CANCELADA: 'Cancelada',
  NAO_COMPARECEU: 'Não compareceu',
};

@Component({
  selector: 'app-consultas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel, SelectModule, DatePickerModule],
  template: `
    <app-resource-table-page
      title="Consultas"
      subtitle="Agenda consolidada das consultas registradas."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma consulta encontrada."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      width="600px"
      (visibleChange)="dialogVisible.set($event)"
      (confirm)="saveDialog()"
      (cancel)="closeDialog()"
    >
      <div class="flex flex-col gap-5 pt-2">
        <p-floatlabel variant="on">
          <input pInputText id="usuarioId" [(ngModel)]="formUsuarioId" style="width:100%" />
          <label for="usuarioId">ID do Usuário (Paciente) *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="medicoId" [(ngModel)]="formMedicoId" style="width:100%" />
          <label for="medicoId">ID do Médico *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="consultorioId" [(ngModel)]="formConsultorioId" style="width:100%" />
          <label for="consultorioId">ID do Consultório *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="alocacaoMedicoId" [(ngModel)]="formAlocacaoMedicoId" style="width:100%" />
          <label for="alocacaoMedicoId">ID da Alocação Médica *</label>
        </p-floatlabel>
        <div class="grid grid-cols-2 gap-4">
          <p-floatlabel variant="on">
            <p-datepicker id="dataHoraInicio" [(ngModel)]="formDataHoraInicio" [showTime]="true" dateFormat="dd/mm/yy" style="width:100%" />
            <label for="dataHoraInicio">Data/hora início *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <p-datepicker id="dataHoraFim" [(ngModel)]="formDataHoraFim" [showTime]="true" dateFormat="dd/mm/yy" style="width:100%" />
            <label for="dataHoraFim">Data/hora fim *</label>
          </p-floatlabel>
        </div>
        <p-floatlabel variant="on">
          <p-select id="status" [(ngModel)]="formStatus" [options]="statusOptions" optionLabel="label" optionValue="value" style="width:100%" />
          <label for="status">Status *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="tipoConsulta" [(ngModel)]="formTipoConsulta" style="width:100%" />
          <label for="tipoConsulta">Tipo de consulta *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="motivo" [(ngModel)]="formMotivo" style="width:100%" />
          <label for="motivo">Motivo</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class ConsultasPage extends ResourceCrudPageBase<Consulta, ConsultaInput> {
  protected readonly service = inject(ConsultaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly statusOptions = STATUS_OPTIONS;
  formUsuarioId = signal('');
  formMedicoId = signal('');
  formConsultorioId = signal('');
  formAlocacaoMedicoId = signal('');
  formDataHoraInicio = signal<Date | null>(null);
  formDataHoraFim = signal<Date | null>(null);
  formStatus = signal<StatusConsulta>('AGENDADA');
  formTipoConsulta = signal('');
  formMotivo = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'status', header: 'Status' },
    { field: 'dataHoraInicio', header: 'Início' },
    { field: 'dataHoraFim', header: 'Fim' },
    { field: 'tipoConsulta', header: 'Tipo' },
    { field: 'motivo', header: 'Motivo' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['consulta', 'create']) ? 'Nova consulta' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['consulta', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((c) => ({
      id: c.id,
      values: {
        status: STATUS_LABEL[c.status] ?? c.status,
        dataHoraInicio: formatDateTime(c.dataHoraInicio),
        dataHoraFim: formatDateTime(c.dataHoraFim),
        tipoConsulta: c.tipoConsulta,
        motivo: c.motivo,
      },
    })),
  );

  protected createTitle() { return 'Nova consulta'; }
  protected editTitle() { return 'Editar consulta'; }

  buildCreateInput(): ConsultaInput {
    return {
      usuarioId: this.formUsuarioId(),
      medicoId: this.formMedicoId(),
      consultorioId: this.formConsultorioId(),
      alocacaoMedicoId: this.formAlocacaoMedicoId(),
      dataHoraInicio: this.formDataHoraInicio()?.toISOString() ?? '',
      dataHoraFim: this.formDataHoraFim()?.toISOString() ?? '',
      status: this.formStatus(),
      tipoConsulta: this.formTipoConsulta(),
      motivo: this.formMotivo(),
    };
  }
  buildEditInput(): ConsultaInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((c) => c.id === id);
    if (!e) return;
    this.formUsuarioId.set(e.usuarioId);
    this.formMedicoId.set(e.medicoId);
    this.formConsultorioId.set(e.consultorioId);
    this.formAlocacaoMedicoId.set(e.alocacaoMedicoId);
    this.formDataHoraInicio.set(e.dataHoraInicio ? new Date(e.dataHoraInicio) : null);
    this.formDataHoraFim.set(e.dataHoraFim ? new Date(e.dataHoraFim) : null);
    this.formStatus.set(e.status);
    this.formTipoConsulta.set(e.tipoConsulta);
    this.formMotivo.set(e.motivo);
  }

  override openCreateDialog(): void {
    this.formUsuarioId.set(''); this.formMedicoId.set(''); this.formConsultorioId.set('');
    this.formAlocacaoMedicoId.set(''); this.formDataHoraInicio.set(null); this.formDataHoraFim.set(null);
    this.formStatus.set('AGENDADA'); this.formTipoConsulta.set(''); this.formMotivo.set('');
    super.openCreateDialog();
  }
}
