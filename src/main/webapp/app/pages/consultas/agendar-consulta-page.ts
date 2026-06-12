import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonDirective } from 'primeng/button';
import { DatePicker } from 'primeng/datepicker';
import { Skeleton } from 'primeng/skeleton';
import { Textarea } from 'primeng/textarea';
import { Toast } from 'primeng/toast';
import { DemoMedflowDataService } from '../../@core/mock/demo-medflow-data.service';
import { AuthenticationService } from '../../@core/security/authentication/authentication.service';
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
  private readonly messageService = inject(MessageService);
  private readonly auth = inject(AuthenticationService);
  private readonly demoData = inject(DemoMedflowDataService);

  readonly step = signal<Step>(0);
  readonly confirmed = signal(false);
  readonly saving = signal(false);

  readonly selectedEspecialidadeId = signal<string | null>(null);
  readonly selectedUnidadeId = signal<string | null>(null);
  readonly selectedMedicoId = signal<string | null>(null);
  readonly selectedData = signal<Date | null>(null);
  readonly selectedHora = signal<string | null>(null);
  readonly motivo = signal('');

  readonly especialidadesResource = this.demoData.createResource(() => ({
    content: this.demoData.list('especialidade').content as readonly Especialidade[],
    page: 0,
    size: 50,
    totalElements: this.demoData.list('especialidade').totalElements,
    totalPages: 1,
  }));
  readonly unidadesResource = this.demoData.createResource(() => ({
    content: this.demoData.list('unidade').content as readonly Unidade[],
    page: 0,
    size: 50,
    totalElements: this.demoData.list('unidade').totalElements,
    totalPages: 1,
  }));
  readonly medicosResource = this.demoData.createResource(() => ({
    content: this.demoData.list('medico').content as readonly Medico[],
    page: 0,
    size: 50,
    totalElements: this.demoData.list('medico').totalElements,
    totalPages: 1,
  }));

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
      const medicoId = this.selectedMedicoId()!;
      const consultorioId = this.demoData.findAlocacaoByMedicoId(medicoId)?.consultorioId ?? '';
      const usuarioId = this.demoData.findUsuarioByKeycloakId(this.auth.user()?.id ?? null)?.id ?? '';

      const input: ConsultaInput = {
        usuarioId,
        medicoId,
        consultorioId,
        alocacaoMedicoId: this.demoData.findAlocacaoByMedicoId(medicoId)?.id ?? '',
        dataHoraInicio: inicio.toISOString(),
        dataHoraFim: fim.toISOString(),
        status: 'AGENDADA',
        tipoConsulta: this.getEspecialidadeNome(),
        motivo: this.motivo(),
      };

      const entity: Consulta = this.demoData.agendarConsulta(
        input,
        usuarioId,
        consultorioId,
        input.alocacaoMedicoId,
      );
      this.confirmed.set(!!entity.id);
      this.medicosResource.reload();
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
