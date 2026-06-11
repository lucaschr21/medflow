import { DatePipe } from '@angular/common';
import { HttpClient, httpResource } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonDirective } from 'primeng/button';
import { DatePicker } from 'primeng/datepicker';
import { Skeleton } from 'primeng/skeleton';
import { Textarea } from 'primeng/textarea';
import { Toast } from 'primeng/toast';
import { firstValueFrom } from 'rxjs';

import type { PageResult } from '../../@core/persistence/page-result';
import { environment } from '../../environments/environment';
import type { Consulta, ConsultaInput } from '../../schemas/consulta.schema';
import type { Especialidade } from '../../schemas/especialidade.schema';
import type { Medico } from '../../schemas/medico.schema';
import type { Unidade } from '../../schemas/unidade.schema';

type Step = 0 | 1 | 2 | 3 | 4;

@Component({
  selector: 'app-agendar-consulta-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    RouterLink,
    DatePipe,
    ButtonDirective,
    DatePicker,
    Textarea,
    Skeleton,
    FormsModule,
    Toast,
  ],
  templateUrl: './agendar-consulta-page.html',
})
export class AgendarConsultaPage {
  readonly STEPS: { label: string }[] = [
    { label: 'Especialidade' },
    { label: 'Unidade' },
    { label: 'Médico' },
    { label: 'Data e Horário' },
    { label: 'Confirmação' },
  ];
  readonly today = new Date();

  private readonly router = inject(Router);
  private readonly http = inject(HttpClient);
  private readonly messageService = inject(MessageService);

  readonly step = signal<Step>(0);
  readonly confirmed = signal(false);
  readonly saving = signal(false);

  readonly selectedEspecialidadeId = signal<string | null>(null);
  readonly selectedUnidadeId = signal<string | null>(null);
  readonly selectedMedicoId = signal<string | null>(null);
  readonly selectedData = signal<Date | null>(null);
  readonly selectedHora = signal<string | null>(null);
  readonly motivo = signal('');

  readonly especialidadesResource = httpResource<PageResult<Especialidade>>(
    () => `${environment.api.baseUrl}/especialidades?page=0&size=50`,
  );
  readonly unidadesResource = httpResource<PageResult<Unidade>>(
    () => `${environment.api.baseUrl}/unidades?page=0&size=50`,
  );
  readonly medicosResource = httpResource<PageResult<Medico>>(
    () => `${environment.api.baseUrl}/medicos?page=0&size=50`,
  );

  readonly hours = Array.from({ length: 17 }, (_, i) => {
    const h = i + 7;
    return `${h.toString().padStart(2, '0')}:00`;
  });

  readonly canGoNext = computed(() => {
    const s = this.step();
    if (s === 0) return !!this.selectedEspecialidadeId();
    if (s === 1) return !!this.selectedUnidadeId();
    if (s === 2) return !!this.selectedMedicoId();
    if (s === 3) return !!this.selectedData() && !!this.selectedHora();
    return true;
  });

  getEspecialidadeNome(): string {
    return (
      this.especialidadesResource
        .value()
        ?.content?.find((e) => e.id === this.selectedEspecialidadeId())?.nome ?? '—'
    );
  }
  getUnidadeNome(): string {
    return (
      this.unidadesResource.value()?.content?.find((u) => u.id === this.selectedUnidadeId())
        ?.nome ?? '—'
    );
  }
  getMedicoNome(): string {
    const m = this.medicosResource
      .value()
      ?.content?.find((md) => md.id === this.selectedMedicoId());
    return m ? `Médico ${m.id}` : '—';
  }

  goNext(): void {
    this.step.update((s) => Math.min(s + 1, 4) as Step);
  }
  goPrev(): void {
    this.step.update((s) => Math.max(s - 1, 0) as Step);
  }

  async confirm(): Promise<void> {
    if (this.saving()) return;
    this.saving.set(true);

    try {
      const data = this.selectedData()!;
      const [hora, minuto] = (this.selectedHora() ?? '08:00').split(':').map(Number);

      const inicio = new Date(data.getFullYear(), data.getMonth(), data.getDate(), hora, minuto);
      const fim = new Date(inicio.getTime() + 30 * 60000);

      const input: ConsultaInput = {
        usuarioId: '', // será preenchido pelo backend via usuário autenticado
        medicoId: this.selectedMedicoId()!,
        consultorioId: '', // backend pode inferir da alocação
        alocacaoMedicoId: '', // backend pode inferir
        dataHoraInicio: inicio.toISOString(),
        dataHoraFim: fim.toISOString(),
        status: 'AGENDADA' as const,
        tipoConsulta: this.getEspecialidadeNome(),
        motivo: this.motivo(),
      };

      const result = await firstValueFrom(
        this.http.post<Consulta>(`${environment.api.baseUrl}/consultas/agendar`, input),
      );

      if (result) {
        this.confirmed.set(true);
      }
    } catch {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro ao agendar',
        detail: 'Não foi possível agendar a consulta. Tente novamente.',
      });
    } finally {
      this.saving.set(false);
    }
  }

  goToConsultas(): void {
    this.router.navigate(['/minhas-consultas']);
  }
  novoAgendamento(): void {
    this.confirmed.set(false);
    this.step.set(0);
    this.selectedEspecialidadeId.set(null);
    this.selectedUnidadeId.set(null);
    this.selectedMedicoId.set(null);
    this.selectedData.set(null);
    this.selectedHora.set(null);
    this.motivo.set('');
  }
}
