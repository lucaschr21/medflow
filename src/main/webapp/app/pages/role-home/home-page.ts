import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  AuthenticationService,
  type UserRole,
} from '../../@core/security/authentication/authentication.service';

interface AdminCard {
  label: string;
  description: string;
  href: string;
  icon: string;
  bgClass: string;
  iconColor: string;
}

interface SetupStep {
  icon: string;
  label: string;
  desc: string;
  href: string;
}

@Component({
  selector: 'app-home-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, DatePipe],
  templateUrl: './home-page.html',
})
export class HomePage {
  readonly today = new Date();

  private readonly authService = inject(AuthenticationService);

  readonly role = computed<UserRole | null>(() => this.authService.user()?.role ?? null);
  readonly userName = computed(() => {
    const name = this.authService.displayName();
    if (!name) return '';
    return name.split(' ')[0];
  });

  readonly setupSteps: SetupStep[] = [
    { icon: '🏥', label: 'Organização', desc: 'Dados da clínica', href: '/organizacoes' },
    { icon: '📋', label: 'Especialidades', desc: 'Tipos de consulta', href: '/especialidades' },
    { icon: '🏢', label: 'Unidades', desc: 'Locais', href: '/unidades' },
    { icon: '🚪', label: 'Consultórios', desc: 'Salas', href: '/consultorios' },
    { icon: '👥', label: 'Usuários', desc: 'Equipe', href: '/usuarios' },
    { icon: '👨‍⚕️', label: 'Médicos', desc: 'Especialidades', href: '/medicos' },
    { icon: '📅', label: 'Agenda', desc: 'Horários', href: '/alocacoes-medicas' },
    { icon: '📊', label: 'Consultas', desc: 'Visão geral', href: '/consultas' },
  ];

  readonly adminCards: AdminCard[] = [
    {
      label: 'Usuários',
      description: 'Cadastre médicos, recepcionistas e administradores.',
      href: '/usuarios',
      icon: 'pi pi-users',
      bgClass: 'bg-indigo-50',
      iconColor: 'text-indigo-500',
    },
    {
      label: 'Médicos',
      description: 'Vincule usuários a médicos e especialidades.',
      href: '/medicos',
      icon: 'pi pi-user-plus',
      bgClass: 'bg-primary-50',
      iconColor: 'text-primary',
    },
    {
      label: 'Unidades',
      description: 'Configure os locais de atendimento da clínica.',
      href: '/unidades',
      icon: 'pi pi-building',
      bgClass: 'bg-sky-50',
      iconColor: 'text-sky-500',
    },
    {
      label: 'Especialidades',
      description: 'Gerencie as especialidades médicas oferecidas.',
      href: '/especialidades',
      icon: 'pi pi-book',
      bgClass: 'bg-violet-50',
      iconColor: 'text-violet-500',
    },
    {
      label: 'Consultórios',
      description: 'Configure as salas de atendimento.',
      href: '/consultorios',
      icon: 'pi pi-clone',
      bgClass: 'bg-amber-50',
      iconColor: 'text-amber-500',
    },
    {
      label: 'Agenda médica',
      description: 'Defina horários e alocações dos médicos.',
      href: '/alocacoes-medicas',
      icon: 'pi pi-calendar',
      bgClass: 'bg-emerald-50',
      iconColor: 'text-emerald-500',
    },
  ];
}
