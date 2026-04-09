import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

interface QuickAction {
  id: 'book-consultation';
  title: string;
  description: string;
  icon: string;
}

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, ButtonModule, CardModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  protected readonly activeSection = signal<'inicio' | null>('inicio');

  protected readonly quickActions = signal<QuickAction[]>([
    {
      id: 'book-consultation',
      title: 'Marcar Teleconsulta',
      description: 'Agendamento de teleconsulta',
      icon: 'pi pi-calendar-plus',
    },
  ]);
}
