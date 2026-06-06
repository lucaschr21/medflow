import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import { shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { AgendaMedica } from '../../schemas/agenda-medica.schema';
import { AgendaMedicaService } from '../../services/agenda-medica.service';

@Component({
  selector: 'app-agendas-medicas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage],
  template: `
    <app-resource-table-page
      title="Agenda médica"
      subtitle="Configuração dos horários recorrentes por alocação médica."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma agenda médica encontrada."
    />
  `,
})
export class AgendasMedicasPage extends ResourceListPageBase<AgendaMedica> {
  protected readonly service = inject(AgendaMedicaService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'diaSemana', header: 'Dia da semana' },
    { field: 'horaInicio', header: 'Hora inicial' },
    { field: 'horaFim', header: 'Hora final' },
    { field: 'alocacaoMedicoId', header: 'Alocação' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['agenda-medica', 'create']) ? 'Nova agenda' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((agenda) => ({
      id: agenda.id,
      values: {
        diaSemana: agenda.diaSemana,
        horaInicio: agenda.horaInicio,
        horaFim: agenda.horaFim,
        alocacaoMedicoId: shortId(agenda.alocacaoMedicoId),
      },
    })),
  );
}
