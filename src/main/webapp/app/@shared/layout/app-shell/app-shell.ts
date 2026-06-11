import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Drawer } from 'primeng/drawer';
import { Skeleton } from 'primeng/skeleton';

import { AuthenticationService } from '../../../@core/security/authentication/authentication.service';
import { AuthorizationService } from '../../../@core/security/authorization/authorization.service';
import type { NavItem } from '../nav-config';
import { getNavGroups } from '../nav-config';
import { Sidebar } from '../sidebar/sidebar';
import { Topbar } from '../topbar/topbar';

@Component({
  selector: 'app-shell',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterOutlet, Drawer, Skeleton, Sidebar, Topbar],
  templateUrl: './app-shell.html',
  styleUrl: './app-shell.scss',
})
export class AppShell {
  private readonly authService = inject(AuthenticationService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly ready = this.authService.ready;
  readonly authenticated = this.authService.authenticated;
  readonly loadingPermissions = this.authorizationService.loading;
  readonly mobileMenuVisible = signal(false);

  readonly user = this.authService.user;

  readonly visibleNavGroups = computed(() => {
    const role = this.user()?.role ?? null;
    return getNavGroups(role)
      .map((group) => ({
        ...group,
        items: group.items.filter((item) => this.isVisible(item)),
      }))
      .filter((group) => group.items.length > 0);
  });

  openMobileMenu(): void {
    this.mobileMenuVisible.set(true);
  }

  closeMobileMenu(): void {
    this.mobileMenuVisible.set(false);
  }

  private isVisible(item: NavItem): boolean {
    return item.permission ? this.authorizationService.can(item.permission) : true;
  }
}
