import { DatePipe } from '@angular/common';
import { HttpClient, httpResource } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Skeleton } from 'primeng/skeleton';
import { Tag } from 'primeng/tag';
import { firstValueFrom } from 'rxjs';

import type { PageResult } from '../../@core/persistence/page-result';
import { AuthenticationService } from '../../@core/security/authentication/authentication.service';
import { environment } from '../../environments/environment';
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
  private readonly http = inject(HttpClient);
  readonly role = computed(() => inject(AuthenticationService).user()?.role);
  private readonly base = environment.api.baseUrl;

  readonly consultasResource = httpResource<PageResult<Consulta>>(
    () => `${this.base}/consultas?page=0&size=100`,
  );

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
    try {
      await firstValueFrom(this.http.post(`${this.base}${url}`, {}));
      this.consultasResource.reload();
    } catch {
      /* erro silencioso */
    }
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
