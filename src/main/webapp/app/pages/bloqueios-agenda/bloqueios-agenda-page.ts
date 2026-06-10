import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
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
import type { BloqueioAgenda, BloqueioAgendaInput } from '../../schemas/bloqueio-agenda.schema';
import type { TipoBloqueioAgenda } from '../../schemas/enums/tipo-bloqueio-agenda.enum';
import { BloqueioAgendaService } from '../../services/bloqueio-agenda.service';

const TIPOS: { label: string; value: TipoBloqueioAgenda }[] = [
  { label: 'Pausa', value: 'PAUSA' },
  { label: 'Férias', value: 'FERIAS' },
  { label: 'Indisponibilidade', value: 'INDISPONIBILIDADE' },
  { label: 'Outro', value: 'OUTRO' },
];

@Component({
  selector: 'app-bloqueios-agenda-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, TextareaModule, FloatLabel, SelectModule, DatePickerModule],
  template: `
    <app-resource-table-page
      title="Bloqueios de agenda"
      subtitle="Períodos de indisponibilidade registrados por médico."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum bloqueio encontrado."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      width="540px"
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
          <p-select id="tipo" [(ngModel)]="formTipo" [options]="tipos" optionLabel="label" optionValue="value" style="width:100%" />
          <label for="tipo">Tipo *</label>
        </p-floatlabel>
        <div class="grid grid-cols-2 gap-4">
          <p-floatlabel variant="on">
            <p-datepicker id="inicio" [(ngModel)]="formInicio" [showTime]="true" dateFormat="dd/mm/yy" style="width:100%" />
            <label for="inicio">Início *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <p-datepicker id="fim" [(ngModel)]="formFim" [showTime]="true" dateFormat="dd/mm/yy" style="width:100%" />
            <label for="fim">Fim *</label>
          </p-floatlabel>
        </div>
        <p-floatlabel variant="on">
          <textarea pTextarea id="motivo" [(ngModel)]="formMotivo" rows="2" style="width:100%"></textarea>
          <label for="motivo">Motivo</label>
        </p-floatlabel>
      </div>
    </app-resource-form-dialog>
  `,
})
export class BloqueiosAgendaPage extends ResourceCrudPageBase<BloqueioAgenda, BloqueioAgendaInput> {
  protected readonly service = inject(BloqueioAgendaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly tipos = TIPOS;
  formMedicoId = signal('');
  formConsultorioId = signal('');
  formTipo = signal<TipoBloqueioAgenda>('PAUSA');
  formInicio = signal<Date | null>(null);
  formFim = signal<Date | null>(null);
  formMotivo = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'tipo', header: 'Tipo' },
    { field: 'inicio', header: 'Início' },
    { field: 'fim', header: 'Fim' },
    { field: 'medicoId', header: 'Médico' },
    { field: 'motivo', header: 'Motivo' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['bloqueio-agenda', 'create']) ? 'Novo bloqueio' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['bloqueio-agenda', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((b) => ({
      id: b.id,
      values: {
        tipo: b.tipo,
        inicio: formatDateTime(b.inicio),
        fim: formatDateTime(b.fim),
        medicoId: shortId(b.medicoId),
        motivo: b.motivo,
      },
    })),
  );

  protected createTitle() { return 'Novo bloqueio de agenda'; }
  protected editTitle() { return 'Editar bloqueio de agenda'; }

  buildCreateInput(): BloqueioAgendaInput {
    return {
      medicoId: this.formMedicoId(),
      consultorioId: this.formConsultorioId(),
      tipo: this.formTipo(),
      inicio: this.formInicio()?.toISOString() ?? '',
      fim: this.formFim()?.toISOString() ?? '',
      motivo: this.formMotivo(),
    };
  }
  buildEditInput(): BloqueioAgendaInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((b) => b.id === id);
    if (!e) return;
    this.formMedicoId.set(e.medicoId);
    this.formConsultorioId.set(e.consultorioId);
    this.formTipo.set(e.tipo);
    this.formInicio.set(e.inicio ? new Date(e.inicio) : null);
    this.formFim.set(e.fim ? new Date(e.fim) : null);
    this.formMotivo.set(e.motivo);
  }

  override openCreateDialog(): void {
    this.formMedicoId.set(''); this.formConsultorioId.set('');
    this.formTipo.set('PAUSA'); this.formInicio.set(null); this.formFim.set(null); this.formMotivo.set('');
    super.openCreateDialog();
  }
}
