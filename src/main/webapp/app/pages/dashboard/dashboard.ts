import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Sidebar } from '../../components/sidebar/sidebar';

interface QuickAction {
  id: 'book-consultation';
  title: string;
  description: string;
  icon: string;
}

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, Sidebar, ButtonModule, CardModule],
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

  protected onMenuSelect(section: 'inicio'): void {
    this.activeSection.set(section);
  }
}
