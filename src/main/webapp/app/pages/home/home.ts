import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, ButtonModule, Ripple, CardModule],
  templateUrl: './home.html',
})
export class Home {
  heroActions = signal([
    {
      label: 'Agendar Consulta',
      routerLink: '/login',
      severity: 'secondary' as const,
      size: 'large' as const,
      styleClass: '!rounded-2xl !px-8 !py-4 !font-bold !text-lg shadow-xl',
      outlined: false,
    },
    {
      label: 'Criar Conta',
      routerLink: '/register',
      severity: 'secondary' as const,
      size: 'large' as const,
      styleClass:
        '!rounded-2xl !px-8 !py-4 !font-bold !text-lg !text-primary-contrast hover:!bg-white/10',
      outlined: true,
    },
  ]);
}
