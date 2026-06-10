import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';
import { SelectModule } from 'primeng/select';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { AgendaMedica, AgendaMedicaInput } from '../../schemas/agenda-medica.schema';
import { AgendaMedicaService } from '../../services/agenda-medica.service';

const DIAS_SEMANA = [
  { label: 'Segunda-feira', value: 'SEGUNDA' },
  { label: 'Terça-feira', value: 'TERCA' },
  { label: 'Quarta-feira', value: 'QUARTA' },
  { label: 'Quinta-feira', value: 'QUINTA' },
  { label: 'Sexta-feira', value: 'SEXTA' },
  { label: 'Sábado', value: 'SABADO' },
  { label: 'Domingo', value: 'DOMINGO' },
];

@Component({
  selector: 'app-agendas-medicas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel, SelectModule],
  template: `
    <app-resource-table-page
      title="Agenda médica"
      subtitle="Configuração dos horários recorrentes por alocação médica."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma agenda médica encontrada."
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
          <input pInputText id="alocacaoMedicoId" [(ngModel)]="formAlocacaoMedicoId" style="width:100%" />
          <label for="alocacaoMedicoId">ID da Alocação *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <p-select id="diaSemana" [(ngModel)]="formDiaSemana" [options]="diasSemana" optionLabel="label" optionValue="value" style="width:100%" />
          <label for="diaSemana">Dia da semana *</label>
        </p-floatlabel>
        <div class="grid grid-cols-2 gap-4">
          <p-floatlabel variant="on">
            <input pInputText id="horaInicio" type="time" [(ngModel)]="formHoraInicio" style="width:100%" />
            <label for="horaInicio">Hora início *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <input pInputText id="horaFim" type="time" [(ngModel)]="formHoraFim" style="width:100%" />
            <label for="horaFim">Hora fim *</label>
          </p-floatlabel>
        </div>
      </div>
    </app-resource-form-dialog>
  `,
})
export class AgendasMedicasPage extends ResourceCrudPageBase<AgendaMedica, AgendaMedicaInput> {
  protected readonly service = inject(AgendaMedicaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly diasSemana = DIAS_SEMANA;
  formAlocacaoMedicoId = signal('');
  formDiaSemana = signal('SEGUNDA');
  formHoraInicio = signal('08:00');
  formHoraFim = signal('17:00');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'diaSemana', header: 'Dia da semana' },
    { field: 'horaInicio', header: 'Hora inicial' },
    { field: 'horaFim', header: 'Hora final' },
    { field: 'alocacaoMedicoId', header: 'Alocação' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['agenda-medica', 'create']) ? 'Nova agenda' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['agenda-medica', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((a) => ({
      id: a.id,
      values: {
        diaSemana: a.diaSemana,
        horaInicio: a.horaInicio,
        horaFim: a.horaFim,
        alocacaoMedicoId: shortId(a.alocacaoMedicoId),
      },
    })),
  );

  protected createTitle() { return 'Nova agenda médica'; }
  protected editTitle() { return 'Editar agenda médica'; }

  buildCreateInput(): AgendaMedicaInput {
    return {
      alocacaoMedicoId: this.formAlocacaoMedicoId(),
      diaSemana: this.formDiaSemana(),
      horaInicio: this.formHoraInicio(),
      horaFim: this.formHoraFim(),
    };
  }
  buildEditInput(): AgendaMedicaInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((a) => a.id === id);
    if (!e) return;
    this.formAlocacaoMedicoId.set(e.alocacaoMedicoId);
    this.formDiaSemana.set(e.diaSemana);
    this.formHoraInicio.set(e.horaInicio);
    this.formHoraFim.set(e.horaFim);
  }

  override openCreateDialog(): void {
    this.formAlocacaoMedicoId.set(''); this.formDiaSemana.set('SEGUNDA');
    this.formHoraInicio.set('08:00'); this.formHoraFim.set('17:00');
    super.openCreateDialog();
  }
}
