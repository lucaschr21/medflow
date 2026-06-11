import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  output,
  viewChild,
} from '@angular/core';
import type { MenuItem } from 'primeng/api';
import { Avatar } from 'primeng/avatar';
import { ButtonDirective } from 'primeng/button';
import { Menu } from 'primeng/menu';

import { AuthenticationService } from '../../../@core/security/authentication/authentication.service';

@Component({
  selector: 'app-topbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ButtonDirective, Avatar, Menu],
  templateUrl: './topbar.html',
  styleUrl: './topbar.scss',
})
export class Topbar {
  private readonly authService = inject(AuthenticationService);

  readonly mobileMenuOpen = output<void>();
  readonly userMenuRef = viewChild.required('userMenu', { read: Menu });

  readonly displayName = this.authService.displayName;
  readonly greeting = computed(() => {
    const name = this.displayName();
    if (!name) return 'Medflow';
    const firstName = name.split(' ')[0];
    const hour = new Date().getHours();
    const timeGreeting = hour < 12 ? 'Bom dia' : hour < 18 ? 'Boa tarde' : 'Boa noite';
    return `${timeGreeting}, ${firstName}`;
  });

  readonly userInitials = computed(() => {
    const name = this.displayName();
    if (!name) return '?';
    return name.charAt(0).toUpperCase();
  });

  readonly userMenuItems = computed<MenuItem[]>(() => [
    {
      label: 'Minha conta',
      icon: 'pi pi-user',
      command: () => void this.authService.openAccount(),
    },
    { separator: true },
    {
      label: 'Sair',
      icon: 'pi pi-sign-out',
      command: () => void this.authService.logout(),
    },
  ]);

  toggleUserMenu(event: Event): void {
    this.userMenuRef().toggle(event);
  }
}
