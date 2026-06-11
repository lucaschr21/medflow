import { DatePipe } from '@angular/common';
import { HttpClient, httpResource } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { DatePicker } from 'primeng/datepicker';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Skeleton } from 'primeng/skeleton';
import { Tag } from 'primeng/tag';
import { firstValueFrom } from 'rxjs';

import type { PageResult } from '../../@core/persistence/page-result';
import { AuthenticationService } from '../../@core/security/authentication/authentication.service';
import { environment } from '../../environments/environment';
import type { BloqueioAgenda } from '../../schemas/bloqueio-agenda.schema';
import type { Consultorio } from '../../schemas/consultorio.schema';
import type { Medico } from '../../schemas/medico.schema';

interface TipoOption {
  label: string;
  value: string;
  color: string;
}

const TIPOS: TipoOption[] = [
  { label: 'Pausa', value: 'PAUSA', color: 'bg-amber-100 text-amber-700' },
  { label: 'Férias', value: 'FERIAS', color: 'bg-red-100 text-red-700' },
  {
    label: 'Indisponibilidade',
    value: 'INDISPONIBILIDADE',
    color: 'bg-orange-100 text-orange-700',
  },
  { label: 'Outro', value: 'OUTRO', color: 'bg-gray-100 text-gray-600' },
];

@Component({
  selector: 'app-bloqueios-agenda-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DatePipe, FormsModule, ButtonDirective, DatePicker, InputText, Select, Tag, Skeleton],
  template: `
    <div class="max-w-4xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-surface-900 tracking-tight">Bloqueios de agenda</h1>
          <p class="text-base text-surface-500 mt-1">Períodos em que os médicos não atendem</p>
        </div>
        <button
          pButton
          label="Novo bloqueio"
          icon="pi pi-plus"
          (click)="showForm.set(true)"
          [disabled]="showForm()"
        ></button>
      </div>

      <!-- Inline form -->
      @if (showForm()) {
        <div class="bg-white border border-surface-200 rounded-2xl p-6 shadow-sm space-y-4">
          <h2 class="text-base font-semibold text-surface-900">Novo bloqueio</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            @if (isAdmin()) {
              <div class="flex flex-col gap-1.5">
                <label class="text-sm font-semibold text-surface-700">Médico</label>
                <p-select
                  [options]="medicoOptions()"
                  [(ngModel)]="novoMedicoId"
                  optionLabel="label"
                  optionValue="value"
                  placeholder="Selecione o médico"
                  styleClass="w-full"
                />
              </div>
            }
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Consultório</label>
              <p-select
                [options]="consultorioOptions()"
                [(ngModel)]="novoConsultorioId"
                optionLabel="label"
                optionValue="value"
                placeholder="Selecione o consultório"
                styleClass="w-full"
              />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Início</label>
              <p-datepicker
                [(ngModel)]="novoInicio"
                dateFormat="dd/mm/yy"
                [showTime]="true"
                hourFormat="24"
                styleClass="w-full"
              />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Fim</label>
              <p-datepicker
                [(ngModel)]="novoFim"
                dateFormat="dd/mm/yy"
                [showTime]="true"
                hourFormat="24"
                styleClass="w-full"
              />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Tipo</label>
              <p-select
                [options]="tipos"
                [(ngModel)]="novoTipo"
                optionLabel="label"
                optionValue="value"
                placeholder="Selecione"
                styleClass="w-full"
              />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="text-sm font-semibold text-surface-700">Motivo</label>
              <input
                pInputText
                class="w-full"
                [(ngModel)]="novoMotivo"
                placeholder="Ex: Congresso, férias..."
              />
            </div>
          </div>
          <div class="flex gap-3">
            <button
              pButton
              label="Cancelar"
              severity="secondary"
              (click)="showForm.set(false)"
            ></button>
            <button pButton label="Salvar" (click)="salvar()" [loading]="saving()"></button>
          </div>
        </div>
      }

      <!-- Lista de bloqueios -->
      @if (bloqueiosResource.isLoading()) {
        <div class="space-y-3">
          @for (i of [1, 2, 3]; track i) {
            <div class="bg-white border border-surface-200 rounded-2xl p-5 shadow-sm">
              <p-skeleton width="60%" height="1.5rem" class="mb-2" /><p-skeleton
                width="40%"
                height="1rem"
              />
            </div>
          }
        </div>
      } @else {
        @for (b of bloqueios(); track b.id) {
          <div
            class="bg-white border border-surface-200 rounded-2xl p-5 shadow-sm hover:shadow-md transition-shadow flex items-center gap-4"
          >
            <div
              class="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0"
              [class]="tipoClass(b.tipo)"
            >
              <i [class]="tipoIcon(b.tipo) + ' text-xl'"></i>
            </div>
            <div class="flex-1 min-w-0">
              <div class="flex items-center gap-2 mb-1">
                <p-tag
                  [value]="tipoLabel(b.tipo)"
                  styleClass="text-xs px-2 py-0.5"
                  [severity]="tipoTagSeverity(b.tipo)"
                />
                <span class="text-sm text-surface-500"
                  >{{ b.inicio | date: 'dd/MM HH:mm' }} → {{ b.fim | date: 'dd/MM HH:mm' }}</span
                >
              </div>
              <p class="text-base text-surface-700">{{ b.motivo }}</p>
            </div>
            <button
              pButton
              icon="pi pi-trash"
              severity="danger"
              text
              rounded
              size="small"
              (click)="excluir(b.id)"
              aria-label="Excluir bloqueio"
            ></button>
          </div>
        } @empty {
          <div class="py-16 text-center">
            <div
              class="w-20 h-20 rounded-full bg-surface-100 flex items-center justify-center mx-auto mb-4"
            >
              <i class="pi pi-calendar text-4xl text-surface-300"></i>
            </div>
            <h3 class="text-lg font-semibold text-surface-700 mb-2">Nenhum bloqueio</h3>
            <p class="text-base text-surface-500">A agenda está completamente livre.</p>
          </div>
        }
      }
    </div>
  `,
})
export class BloqueiosAgendaPage {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthenticationService);
  private readonly base = environment.api.baseUrl;

  readonly isMedico = computed(() => this.auth.user()?.role === 'MEDICO');
  readonly isAdmin = computed(() => this.auth.user()?.role === 'ADMINISTRADOR');

  readonly tipos = TIPOS;
  readonly showForm = signal(false);
  readonly saving = signal(false);
  novoMedicoId: string | null = null;
  novoConsultorioId: string | null = null;
  novoInicio: Date | null = null;
  novoFim: Date | null = null;
  novoTipo = 'PAUSA';
  novoMotivo = '';

  readonly bloqueiosResource = httpResource<PageResult<BloqueioAgenda>>(
    () => `${this.base}/bloqueios-agenda?page=0&size=100`,
  );
  readonly medicosResource = httpResource<PageResult<Medico>>(
    () => `${this.base}/medicos?page=0&size=100`,
  );
  readonly consultoriosResource = httpResource<PageResult<Consultorio>>(
    () => `${this.base}/consultorios?page=0&size=100`,
  );

  readonly bloqueios = computed(() => this.bloqueiosResource.value()?.content ?? []);
  readonly medicoOptions = computed(() =>
    (this.medicosResource.value()?.content ?? []).map((m) => ({
      label: 'Médico ' + m.id.substring(0, 8),
      value: m.id,
    })),
  );
  readonly consultorioOptions = computed(() =>
    (this.consultoriosResource.value()?.content ?? []).map((c) => ({
      label: c.nome + ' (Sala ' + c.sala + ')',
      value: c.id,
    })),
  );

  // Para MEDICO: auto-seleciona o próprio médico
  readonly meuMedicoId = computed(() => {
    if (!this.isMedico()) return null;
    const medicos = this.medicosResource.value()?.content ?? [];
    return medicos.length > 0 ? medicos[0].id : null;
  });

  tipoLabel(t: string): string {
    return TIPOS.find((x) => x.value === t)?.label ?? t;
  }
  tipoClass(t: string): string {
    return (
      TIPOS.find((x) => x.value === t)
        ?.color.replace('text-', 'bg-')
        .replace('700', '100')
        .replace('600', '100') ?? 'bg-gray-100'
    );
  }
  tipoIcon(t: string): string {
    const icons: Record<string, string> = {
      PAUSA: 'pi pi-clock',
      FERIAS: 'pi pi-sun',
      INDISPONIBILIDADE: 'pi pi-ban',
      OUTRO: 'pi pi-info-circle',
    };
    return icons[t] ?? 'pi pi-info-circle';
  }
  tipoTagSeverity(t: string): 'warn' | 'danger' | 'info' | 'secondary' {
    const m: Record<string, 'warn' | 'danger' | 'info' | 'secondary'> = {
      PAUSA: 'warn',
      FERIAS: 'danger',
      INDISPONIBILIDADE: 'info',
      OUTRO: 'secondary',
    };
    return m[t] ?? 'secondary';
  }

  async salvar(): Promise<void> {
    if (!this.novoInicio || !this.novoFim) return;
    this.saving.set(true);
    try {
      await firstValueFrom(
        this.http.post(`${this.base}/bloqueios-agenda`, {
          medicoId: this.novoMedicoId || this.meuMedicoId() || '',
          consultorioId: this.novoConsultorioId || '',
          inicio: this.novoInicio!.toISOString(),
          fim: this.novoFim!.toISOString(),
          motivo: this.novoMotivo,
          tipo: this.novoTipo,
        }),
      );
      this.showForm.set(false);
      this.bloqueiosResource.reload();
    } finally {
      this.saving.set(false);
    }
  }

  async excluir(id: string): Promise<void> {
    try {
      await firstValueFrom(this.http.delete(`${this.base}/bloqueios-agenda/${id}`));
      this.bloqueiosResource.reload();
    } catch {
      /* ok */
    }
  }
}
