import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonDirective } from 'primeng/button';

import { DemoMedflowDataService } from '../../@core/mock/demo-medflow-data.service';

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
  private readonly demoData = inject(DemoMedflowDataService);

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

  readonly counts = {
    org: this.demoData.createResource(() => this.demoData.list('organizacao')),
    unidades: this.demoData.createResource(() => this.demoData.list('unidade')),
    consultorios: this.demoData.createResource(() => this.demoData.list('consultorio')),
    especialidades: this.demoData.createResource(() => this.demoData.list('especialidade')),
    usuarios: this.demoData.createResource(() => this.demoData.list('usuario')),
    medicos: this.demoData.createResource(() => this.demoData.list('medico')),
    agenda: this.demoData.createResource(() => this.demoData.list('alocacao-medico')),
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
