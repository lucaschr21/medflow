import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterOutlet } from '@angular/router';
import { Drawer } from 'primeng/drawer';
import { map, of } from 'rxjs';

import { AuthorizationService } from '../../../@core/security/authorization/authorization.service';
import { AuthenticationService } from '../../../@core/security/authentication/authentication.service';
import { OrganizacaoService } from '../../../services/organizacao.service';
import type { NavGroup, NavItem } from '../nav-config';
import { NAV_GROUPS } from '../nav-config';
import { Sidebar } from '../sidebar/sidebar';
import { Topbar } from '../topbar/topbar';

@Component({
  selector: 'app-shell',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterOutlet, Drawer, Sidebar, Topbar],
  templateUrl: './app-shell.html',
  styleUrl: './app-shell.scss',
})
export class AppShell {
  private readonly authenticationService = inject(AuthenticationService);
  private readonly authorizationService = inject(AuthorizationService);
  private readonly organizacaoService = inject(OrganizacaoService);

  readonly ready = this.authenticationService.ready;
  readonly authenticated = this.authenticationService.authenticated;
  readonly loadingPermissions = this.authorizationService.loading;
  readonly mobileMenuVisible = signal(false);
  readonly sidebarCollapsed = signal(false);
  private readonly currentOrganizationResource = rxResource({
    defaultValue: null as string | null,
    stream: () =>
      this.shouldLoadOrganizations()
        ? this.organizacaoService
            .findAll({ page: 0, size: 1 })
            .pipe(map((page) => page.content[0]?.nome ?? null))
        : of(null),
  });
  readonly currentOrganization = computed(() => this.currentOrganizationResource.value());
  readonly visibleNavGroups = computed(() =>
    NAV_GROUPS.map((group) => ({
      ...group,
      items: group.items.filter((item) => this.isVisible(item)),
    })).filter((group) => group.items.length > 0),
  );

  openMobileMenu(): void {
    this.mobileMenuVisible.set(true);
  }

  closeMobileMenu(): void {
    this.mobileMenuVisible.set(false);
  }

  toggleSidebarCollapse(): void {
    this.sidebarCollapsed.update((collapsed) => !collapsed);
  }

  private isVisible(item: NavItem): boolean {
    return item.permission ? this.authorizationService.can(item.permission) : true;
  }

  private shouldLoadOrganizations(): boolean {
    return (
      this.authenticated() &&
      this.authorizationService.initialized() &&
      this.authorizationService.can(['organizacao', 'read'])
    );
  }
}
