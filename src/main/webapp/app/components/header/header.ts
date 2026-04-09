import { DOCUMENT } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import type { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';
import { distinctUntilChanged, filter, fromEvent, map, startWith } from 'rxjs';

import { Sidebar } from '../sidebar/sidebar';

import { ThemeToggle } from './theme-toggle/theme-toggle';

@Component({
  selector: 'app-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, MenubarModule, ButtonModule, MenuModule, ThemeToggle, Sidebar],
  templateUrl: './header.html',
})
export class Header {
  private readonly document = inject(DOCUMENT);
  private readonly router = inject(Router);

  protected readonly isScrolled = toSignal(
    fromEvent(this.document, 'scroll').pipe(
      map(() => this.document.documentElement.scrollTop > 50),
      distinctUntilChanged(),
    ),
    { initialValue: false },
  );

  protected readonly isDashboardRoute = toSignal(
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map((event) => event.urlAfterRedirects.startsWith('/dashboard')),
      startWith(this.router.url.startsWith('/dashboard')),
      distinctUntilChanged(),
    ),
    { initialValue: this.router.url.startsWith('/dashboard') },
  );

  protected readonly items: MenuItem[] = [
    {
      label: 'Início',
      icon: 'pi pi-home',
      routerLink: '/',
    },
  ];
  protected readonly accessAreaItems: MenuItem[] = [
    {
      label: 'Empresa',
      icon: 'pi pi-building',
      routerLink: 'login/empresa',
    },
  ];
}
