import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  output,
  viewChild,
} from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Avatar } from 'primeng/avatar';
import { Menu } from 'primeng/menu';
import type { MenuItem } from 'primeng/api';

import { AuthenticationService } from '../../../@core/security/authentication/authentication.service';

@Component({
  selector: 'app-topbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ButtonDirective, Avatar, Menu],
  templateUrl: './topbar.html',
  styleUrl: './topbar.scss',
})
export class Topbar {
  private readonly authenticationService = inject(AuthenticationService);

  readonly sidebarCollapsed = input.required<boolean>();
  readonly currentOrganization = input<string | null>(null);
  readonly mobileMenuOpen = output<void>();
  readonly sidebarCollapseToggle = output<void>();
  readonly userMenu = viewChild.required(Menu);

  readonly displayName = this.authenticationService.displayName;
  readonly userInitials = computed(() => this.initials(this.displayName() ?? 'MF'));
  readonly userMenuItems = computed<MenuItem[]>(() => [
    {
      label: 'Minha conta',
      icon: 'pi pi-user',
      command: () => void this.authenticationService.openAccount(),
    },
    {
      separator: true,
    },
    {
      label: 'Sair',
      icon: 'pi pi-sign-out',
      command: () => void this.authenticationService.logout(),
    },
  ]);

  toggleUserMenu(event: Event): void {
    this.userMenu().toggle(event);
  }

  openMobileMenu(): void {
    this.mobileMenuOpen.emit();
  }

  toggleSidebarCollapse(): void {
    this.sidebarCollapseToggle.emit();
  }

  private initials(name: string): string {
    const parts = name.trim().split(/\s+/);
    return parts.length === 1
      ? parts[0].slice(0, 2).toUpperCase()
      : `${parts[0][0]}${parts.at(-1)?.[0] ?? ''}`.toUpperCase();
  }
}
