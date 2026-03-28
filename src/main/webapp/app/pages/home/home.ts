import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { CardModule } from 'primeng/card';

interface DashboardMetric {
  id: 'active-clinics' | 'available-doctors' | 'available-specialties';
  label: string;
  value: number;
  helper: string;
  icon: string;
}

interface SpecialtyAvailability {
  id: string;
  name: string;
  availableDoctors: number;
  averageWait: string;
}

interface ActiveClinic {
  id: string;
  name: string;
  city: string;
  address: string;
  availableDoctors: number;
  activePatientsToday: number;
  mainSpecialties: string[];
  photoUrl: string;
}

@Component({
  selector: 'app-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, ButtonModule, Ripple, CardModule],
  templateUrl: './home.html',
})
export class Home {
  protected readonly heroActions = signal([
    {
      label: 'Ver dashboard completo',
      routerLink: '/dashboard',
      severity: 'secondary' as const,
      size: 'large' as const,
      styleClass: '!rounded-2xl !px-8 !py-4 !font-bold !text-lg shadow-xl',
      outlined: false,
    },
    {
      label: 'Buscar especialistas',
      routerLink: '/dashboard',
      severity: 'secondary' as const,
      size: 'large' as const,
      styleClass: '!rounded-2xl !px-8 !py-4 !font-bold !text-lg',
      outlined: true,
    },
  ]);

  // dados mockados p teste
  protected readonly dashboardMetrics = signal<DashboardMetric[]>([
    {
      id: 'active-clinics',
      label: 'Clinicas ativas',
      value: 18,
      helper: 'Unidades atendendo pacientes neste momento.',
      icon: 'pi pi-building-columns',
    },
    {
      id: 'available-doctors',
      label: 'Medicos disponiveis',
      value: 246,
      helper: 'Profissionais com agenda aberta para consulta.',
      icon: 'pi pi-users',
    },
    {
      id: 'available-specialties',
      label: 'Especialidades disponiveis',
      value: 27,
      helper: 'Areas medicas com atendimento ativo.',
      icon: 'pi pi-heart',
    },
  ]);

  // dados mock para teste
  protected readonly activeClinics = signal<ActiveClinic[]>([
    {
      id: 'clinica-vida-plena',
      name: 'Clinica Vida Plena',
      city: 'Sao Paulo - SP',
      address: 'Av. Paulista, 1200',
      availableDoctors: 34,
      activePatientsToday: 182,
      mainSpecialties: ['Cardiologia', 'Dermatologia', 'Clinica Geral'],
      photoUrl: 'https://placehold.co/800x500?text=Clinica+Vida+Plena',
    },
    {
      id: 'clinica-santa-helena',
      name: 'Clinica Santa Helena',
      city: 'Belo Horizonte - MG',
      address: 'Rua Paraiba, 455',
      availableDoctors: 27,
      activePatientsToday: 129,
      mainSpecialties: ['Pediatria', 'Neurologia', 'Ortopedia'],
      photoUrl: 'https://placehold.co/800x500?text=Clinica+Santa+Helena',
    },
    {
      id: 'centro-medico-nova-saude',
      name: 'Centro Medico Nova Saude',
      city: 'Curitiba - PR',
      address: 'Alameda Cabral, 98',
      availableDoctors: 31,
      activePatientsToday: 153,
      mainSpecialties: ['Ginecologia', 'Endocrinologia', 'Psiquiatria'],
      photoUrl: 'https://placehold.co/800x500?text=Centro+Medico+Nova+Saude',
    },
  ]);

  //dados mockados para teste
  protected readonly specialties = signal<SpecialtyAvailability[]>([
    {
      id: 'cardiologia',
      name: 'Cardiologia',
      availableDoctors: 32,
      averageWait: '2 dias',
    },
    {
      id: 'dermatologia',
      name: 'Dermatologia',
      availableDoctors: 18,
      averageWait: '3 dias',
    },
    {
      id: 'ginecologia',
      name: 'Ginecologia',
      availableDoctors: 24,
      averageWait: '1 dia',
    },
    {
      id: 'pediatria',
      name: 'Pediatria',
      availableDoctors: 29,
      averageWait: '1 dia',
    },
    {
      id: 'ortopedia',
      name: 'Ortopedia',
      availableDoctors: 21,
      averageWait: '2 dias',
    },
    {
      id: 'neurologia',
      name: 'Neurologia',
      availableDoctors: 14,
      averageWait: '4 dias',
    },
  ]);
}
