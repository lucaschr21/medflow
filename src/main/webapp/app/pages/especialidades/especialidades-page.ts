import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { InputText } from 'primeng/inputtext';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { orDash } from '../../@shared/resource/resource-formatters';
import { ResourceListPageBase } from '../../@shared/resource/resource-list-page.base';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Especialidade } from '../../schemas/especialidade.schema';
import { EspecialidadeService } from '../../services/especialidade.service';

@Component({
  selector: 'app-especialidades-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (showForm) {
      <div
        class="max-w-lg mx-auto mb-6 bg-white border border-surface-200 rounded-2xl p-6 shadow-sm"
      >
        <h2 class="text-lg font-semibold text-surface-900 mb-4">Nova especialidade</h2>
        <div class="space-y-4">
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Nome</label>
            <input pInputText class="w-full" [(ngModel)]="newNome" placeholder="Ex: Cardiologia" />
          </div>
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Descrição</label>
            <input
              pInputText
              class="w-full"
              [(ngModel)]="newDescricao"
              placeholder="Descrição da especialidade"
            />
          </div>
          <div class="flex gap-3">
            <button
              pButton
              label="Cancelar"
              severity="secondary"
              (click)="showForm = false"
            ></button>
            <button
              pButton
              label="Salvar"
              (click)="salvarEspecialidade()"
              [loading]="saving"
            ></button>
          </div>
        </div>
      </div>
    }
    <app-resource-table-page
      title="Especialidades"
      subtitle="Lista das especialidades médicas cadastradas."
      [createLabel]="createLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma especialidade encontrada."
      (createClick)="showForm = true"
    />
  `,
  imports: [ResourceTablePage, FormsModule, ButtonDirective, InputText],
})
export class EspecialidadesPage extends ResourceListPageBase<Especialidade> {
  protected readonly service = inject(EspecialidadeService);
  private readonly authorizationService = inject(AuthorizationService);

  showForm = false;
  saving = false;
  newNome = '';
  newDescricao = '';

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'descricao', header: 'Descrição' },
  ];
  readonly createLabel = computed(() =>
    this.authorizationService.can(['especialidade', 'create']) ? 'Nova especialidade' : null,
  );
  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((especialidade) => ({
      id: especialidade.id,
      values: {
        nome: especialidade.nome,
        descricao: orDash(especialidade.descricao),
      },
    })),
  );

  async salvarEspecialidade(): Promise<void> {
    if (!this.newNome.trim()) return;
    this.saving = true;
    try {
      await this.service
        .create({ nome: this.newNome, descricao: this.newDescricao } as any)
        .toPromise();
      this.showForm = false;
      this.newNome = '';
      this.newDescricao = '';
      this.pageResource.reload();
    } finally {
      this.saving = false;
    }
  }
}
