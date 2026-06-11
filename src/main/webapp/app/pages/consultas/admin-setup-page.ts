import { httpResource } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonDirective } from 'primeng/button';

import type { PageResult } from '../../@core/persistence/page-result';
import { environment } from '../../environments/environment';

interface SetupStep {
  id: string;
  label: string;
  description: string;
  href: string;
}

@Component({
  selector: 'app-admin-setup-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, ButtonDirective],
  templateUrl: './admin-setup-page.html',
})
export class AdminSetupPage {
  readonly steps: SetupStep[] = [
    {
      id: 'org',
      label: 'Configurar organização',
      description: 'Defina nome e dados da clínica',
      href: '/organizacoes',
    },
    {
      id: 'unidades',
      label: 'Cadastrar unidades',
      description: 'Configure as unidades de atendimento',
      href: '/unidades',
    },
    {
      id: 'consultorios',
      label: 'Cadastrar consultórios',
      description: 'Configure as salas de atendimento',
      href: '/consultorios',
    },
    {
      id: 'especialidades',
      label: 'Cadastrar especialidades',
      description: 'Defina as especialidades médicas',
      href: '/especialidades',
    },
    {
      id: 'usuarios',
      label: 'Cadastrar usuários',
      description: 'Crie usuários para médicos e recepcionistas',
      href: '/usuarios',
    },
    {
      id: 'medicos',
      label: 'Cadastrar médicos',
      description: 'Vincule usuários a médicos',
      href: '/medicos',
    },
    {
      id: 'agenda',
      label: 'Configurar agenda',
      description: 'Alocações, horários e dias',
      href: '/alocacoes-medicas',
    },
  ];

  private base = environment.api.baseUrl;

  readonly counts = {
    org: httpResource<PageResult<unknown>>(() => `${this.base}/organizacoes?page=0&size=1`),
    unidades: httpResource<PageResult<unknown>>(() => `${this.base}/unidades?page=0&size=1`),
    consultorios: httpResource<PageResult<unknown>>(
      () => `${this.base}/consultorios?page=0&size=1`,
    ),
    especialidades: httpResource<PageResult<unknown>>(
      () => `${this.base}/especialidades?page=0&size=1`,
    ),
    usuarios: httpResource<PageResult<unknown>>(() => `${this.base}/usuarios?page=0&size=1`),
    medicos: httpResource<PageResult<unknown>>(() => `${this.base}/medicos?page=0&size=1`),
    agenda: httpResource<PageResult<unknown>>(() => `${this.base}/alocacoes-medicas?page=0&size=1`),
  };

  getStatus(id: string): 'concluido' | 'pendente' {
    const r = (this.counts as Record<string, any>)[id];
    return r?.value()?.totalElements > 0 ? 'concluido' : 'pendente';
  }

  readonly concluidos = computed(
    () => this.steps.filter((s) => this.getStatus(s.id) === 'concluido').length,
  );
  readonly progresso = computed(() => Math.round((this.concluidos() / this.steps.length) * 100));
}
