import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Sidebar } from '../../components/sidebar/sidebar';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, Sidebar, ButtonModule, CardModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  protected readonly activeSection = signal<'inicio' | null>(null);

  protected onMenuSelect(section: 'inicio'): void {
    this.activeSection.set(section);
  }
}
