import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Skeleton } from 'primeng/skeleton';
import { Tag } from 'primeng/tag';

import { AuthenticationService } from '../../@core/security/authentication/authentication.service';
import { DemoMedflowDataService } from '../../@core/mock/demo-medflow-data.service';
import type { Consulta } from '../../schemas/consulta.schema';

interface StatusGroup {
  key: string;
  label: string;
  statuses: string[];
  icon: string;
}

const STATUS_GROUPS: StatusGroup[] = [
  { key: 'espera', label: 'Em espera', statuses: ['EM_ESPERA'], icon: 'pi pi-clock' },
  { key: 'atendimento', label: 'Em atendimento', statuses: ['EM_ATENDIMENTO'], icon: 'pi pi-play' },
  {
    key: 'confirmadas',
    label: 'Confirmadas / Aguardando',
    statuses: ['CONFIRMADA', 'AGENDADA'],
    icon: 'pi pi-check-circle',
  },
  {
    key: 'concluidas',
    label: 'Concluídas',
    statuses: ['FINALIZADA', 'NAO_COMPARECEU', 'CANCELADA'],
    icon: 'pi pi-flag',
  },
];

@Component({
  selector: 'app-fila-atendimento-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DatePipe, ButtonDirective, Tag, Skeleton],
  templateUrl: './fila-atendimento-page.html',
})
export class FilaAtendimentoPage {
  readonly today = new Date();
  private readonly demoData = inject(DemoMedflowDataService);
  private readonly auth = inject(AuthenticationService);
  readonly role = computed(() => this.auth.user()?.role);
  readonly medicoAtual = computed(() => this.demoData.findMedicoByKeycloakId(this.auth.user()?.id ?? null));
  readonly consultasResource = this.demoData.createResource(() => ({
    content:
      this.role() === 'MEDICO' && this.medicoAtual()
        ? this.demoData.consultasDoMedicoHoje(this.medicoAtual()!.id)
        : this.demoData.consultasHoje(),
    page: 0,
    size: 100,
    totalElements:
      this.role() === 'MEDICO' && this.medicoAtual()
        ? this.demoData.consultasDoMedicoHoje(this.medicoAtual()!.id).length
        : this.demoData.consultasHoje().length,
    totalPages: 1,
  }));

  readonly grouped = computed(() => {
    const consultas = this.consultasResource.value()?.content ?? [];
    return STATUS_GROUPS.map((g) => ({
      ...g,
      items: consultas.filter((c) => g.statuses.includes(c.status)),
    }));
  });

  statusLabel(s: string): string {
    const m: Record<string, string> = {
      AGENDADA: 'Agendada',
      CONFIRMADA: 'Confirmada',
      EM_ESPERA: 'Em espera',
      EM_ATENDIMENTO: 'Atendimento',
      FINALIZADA: 'Finalizada',
      CANCELADA: 'Cancelada',
      NAO_COMPARECEU: 'Faltou',
    };
    return m[s] ?? s;
  }

  canCheckin(c: Consulta): boolean {
    return (
      (c.status === 'AGENDADA' || c.status === 'CONFIRMADA') && this.role() === 'RECEPCIONISTA'
    );
  }

  canIniciar(c: Consulta): boolean {
    return c.status === 'EM_ESPERA' && this.role() === 'MEDICO';
  }

  canFinalizar(c: Consulta): boolean {
    return c.status === 'EM_ATENDIMENTO' && this.role() === 'MEDICO';
  }

  canEmEspera(c: Consulta): boolean {
    return c.status === 'CONFIRMADA' && this.role() === 'RECEPCIONISTA';
  }

  canNaoCompareceu(c: Consulta): boolean {
    return (
      (c.status === 'AGENDADA' || c.status === 'CONFIRMADA' || c.status === 'EM_ESPERA') &&
      this.role() === 'RECEPCIONISTA'
    );
  }

  async acao(url: string): Promise<void> {
    const id = url.split('/').filter(Boolean).at(-2);
    if (!id) return;

    if (url.endsWith('/check-in')) this.demoData.markCheckIn(id);
    if (url.endsWith('/iniciar-atendimento')) this.demoData.markIniciarAtendimento(id);
    if (url.endsWith('/finalizar')) this.demoData.markFinalizar(id);
    if (url.endsWith('/em-espera')) this.demoData.markEmEspera(id);
    if (url.endsWith('/nao-compareceu')) this.demoData.markNaoCompareceu(id);
    this.consultasResource.reload();
  }

  checkin(id: string): void {
    this.acao(`/consultas/${id}/check-in`);
  }
  iniciar(id: string): void {
    this.acao(`/consultas/${id}/iniciar-atendimento`);
  }
  finalizar(id: string): void {
    this.acao(`/consultas/${id}/finalizar`);
  }
  emEspera(id: string): void {
    this.acao(`/consultas/${id}/em-espera`);
  }
  naoCompareceu(id: string): void {
    this.acao(`/consultas/${id}/nao-compareceu`);
  }
}
