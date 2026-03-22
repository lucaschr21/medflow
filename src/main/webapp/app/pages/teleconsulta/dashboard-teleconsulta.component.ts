import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { CarouselModule } from 'primeng/carousel';
import { AvatarModule } from 'primeng/avatar';

@Component({
  selector: 'app-dashboard-teleconsulta',
  standalone: true,
  imports: [CommonModule, ButtonModule, CarouselModule, AvatarModule],
  templateUrl: './dashboard-teleconsulta.component.html',
})
export class DashboardTeleconsultaComponent {
  consultasHistorico = [
    { data: '12/03/2025 20:49', especialidade: 'medico', medico: 'eric ric' },
    { data: '12/03/2025 20:45', especialidade: 'medico', medico: 'joao' },
    { data: '28/08/2024 19:18', especialidade: 'medico', medico: 'lc' },
    { data: '21/05/2024 10:00', especialidade: 'medico', medico: 'luiz caralho' },
  ];

  responsiveOptions = [
    { breakpoint: '1199px', numVisible: 3, numScroll: 1 },
    { breakpoint: '991px', numVisible: 2, numScroll: 1 },
    { breakpoint: '767px', numVisible: 1, numScroll: 1 },
  ];
}
