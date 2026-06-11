import { DatePipe } from '@angular/common';
import { HttpClient, httpResource } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { Select } from 'primeng/select';
import { Skeleton } from 'primeng/skeleton';
import { firstValueFrom } from 'rxjs';

import type { PageResult } from '../../@core/persistence/page-result';
import { environment } from '../../environments/environment';
import type { AgendaMedica } from '../../schemas/agenda-medica.schema';

const DIAS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
const DIA_LABELS: Record<string, string> = {
  MONDAY: 'Seg',
  TUESDAY: 'Ter',
  WEDNESDAY: 'Qua',
  THURSDAY: 'Qui',
  FRIDAY: 'Sex',
  SATURDAY: 'Sáb',
  SUNDAY: 'Dom',
};
const HORAS = Array.from({ length: 24 }, (_, i) => ({
  label: `${String(i).padStart(2, '0')}:00`,
  value: `${String(i).padStart(2, '0')}:00`,
}));

interface GroupedAgenda {
  alocacaoId: string;
  dias: Record<string, AgendaMedica[]>;
}

@Component({
  selector: 'app-agendas-medicas-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DatePipe, FormsModule, ButtonDirective, Select, Skeleton],
  template: `
    <div class="max-w-5xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-surface-900 tracking-tight">Agenda médica</h1>
          <p class="text-base text-surface-500 mt-1">Horários semanais de atendimento</p>
        </div>
        <button
          pButton
          label="Adicionar horário"
          icon="pi pi-plus"
          (click)="showForm.set(!showForm())"
        ></button>
      </div>

      <!-- Inline form -->
      @if (showForm()) {
        <div class="bg-white border border-surface-200 rounded-2xl p-6 shadow-sm space-y-4">
          <h2 class="text-base font-semibold text-surface-900">Novo horário</h2>
          <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Dia da semana</label>
              <p-select
                [options]="diaOptions"
                [(ngModel)]="novoDia"
                optionLabel="label"
                optionValue="value"
                styleClass="w-full"
              />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Hora início</label>
              <p-select
                [options]="horas"
                [(ngModel)]="novoInicio"
                optionLabel="label"
                optionValue="value"
                styleClass="w-full"
              />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Hora fim</label>
              <p-select
                [options]="horas"
                [(ngModel)]="novoFim"
                optionLabel="label"
                optionValue="value"
                styleClass="w-full"
              />
            </div>
            <div class="flex items-end">
              <button
                pButton
                label="Salvar"
                (click)="salvar()"
                [loading]="saving()"
                class="w-full"
              ></button>
            </div>
          </div>
        </div>
      }

      <!-- Weekly grid -->
      @if (agendasResource.isLoading()) {
        <p-skeleton width="100%" height="12rem" />
      } @else if (agrupado().length === 0) {
        <div class="py-16 text-center">
          <div
            class="w-20 h-20 rounded-full bg-surface-100 flex items-center justify-center mx-auto mb-4"
          >
            <i class="pi pi-calendar text-4xl text-surface-300"></i>
          </div>
          <h3 class="text-lg font-semibold text-surface-700 mb-2">Nenhum horário configurado</h3>
          <p class="text-base text-surface-500">
            Adicione horários de atendimento para os médicos.
          </p>
        </div>
      } @else {
        @for (grupo of agrupado(); track grupo.alocacaoId) {
          <div class="bg-white border border-surface-200 rounded-2xl shadow-sm overflow-hidden">
            <div class="px-5 py-3 bg-surface-50 border-b border-surface-100">
              <span class="text-sm font-semibold text-surface-600"
                >Alocação {{ grupo.alocacaoId.substring(0, 8) }}...</span
              >
            </div>
            <div class="grid grid-cols-7 divide-x divide-surface-100">
              @for (dia of DIAS; track dia) {
                <div class="p-3 min-h-[5rem]">
                  <div class="text-xs font-bold text-surface-400 uppercase mb-2 text-center">
                    {{ DIA_LABELS[dia] }}
                  </div>
                  @for (agenda of grupo.dias[dia] ?? []; track agenda.id) {
                    <div
                      class="bg-primary-50 border border-primary-200 rounded-lg px-2 py-1.5 text-center mb-1 group relative"
                    >
                      <p class="text-xs font-semibold text-primary-700">
                        {{ agenda.horaInicio }} - {{ agenda.horaFim }}
                      </p>
                      <button
                        pButton
                        icon="pi pi-times"
                        severity="danger"
                        text
                        rounded
                        size="small"
                        class="absolute -top-1 -right-1 opacity-0 group-hover:opacity-100 transition-opacity"
                        (click)="excluir(agenda.id)"
                        aria-label="Remover horário"
                      ></button>
                    </div>
                  } @empty {
                    <div class="text-xs text-surface-300 text-center py-2">—</div>
                  }
                </div>
              }
            </div>
          </div>
        }
      }
    </div>
  `,
})
export class AgendasMedicasPage {
  private readonly http = inject(HttpClient);
  private readonly base = environment.api.baseUrl;

  readonly DIAS = DIAS;
  readonly DIA_LABELS = DIA_LABELS;
  readonly horas = HORAS;
  readonly diaOptions = DIAS.map((d) => ({ label: DIA_LABELS[d] + ' - ' + d, value: d }));

  readonly showForm = signal(false);
  readonly saving = signal(false);
  novoDia = 'MONDAY';
  novoInicio = '08:00';
  novoFim = '17:00';

  readonly agendasResource = httpResource<PageResult<AgendaMedica>>(
    () => `${this.base}/agendas-medicas?page=0&size=200`,
  );
  readonly agrupado = computed(() => {
    const list = this.agendasResource.value()?.content ?? [];
    const map = new Map<string, Record<string, AgendaMedica[]>>();
    for (const a of list) {
      const key = a.alocacaoMedicoId;
      if (!map.has(key)) map.set(key, {});
      const dias = map.get(key)!;
      if (!dias[a.diaSemana]) dias[a.diaSemana] = [];
      dias[a.diaSemana].push(a);
    }
    return Array.from(map.entries()).map(([alocacaoId, dias]) => ({ alocacaoId, dias }));
  });

  async salvar(): Promise<void> {
    this.saving.set(true);
    try {
      await firstValueFrom(
        this.http.post(`${this.base}/agendas-medicas`, {
          alocacaoMedicoId: '',
          diaSemana: this.novoDia,
          horaInicio: this.novoInicio,
          horaFim: this.novoFim,
        }),
      );
      this.showForm.set(false);
      this.agendasResource.reload();
    } finally {
      this.saving.set(false);
    }
  }

  async excluir(id: string): Promise<void> {
    try {
      await firstValueFrom(this.http.delete(`${this.base}/agendas-medicas/${id}`));
      this.agendasResource.reload();
    } catch {
      /* ok */
    }
  }
}
