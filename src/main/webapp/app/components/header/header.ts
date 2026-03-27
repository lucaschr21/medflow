import { DOCUMENT } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterModule } from '@angular/router';
import type { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';
import { distinctUntilChanged, fromEvent, map } from 'rxjs';

import { ThemeToggle } from './theme-toggle/theme-toggle';

@Component({
  selector: 'app-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, MenubarModule, ButtonModule, MenuModule, ThemeToggle],
  templateUrl: './header.html',
})
export class Header {
  private readonly document = inject(DOCUMENT);

  protected readonly isScrolled = toSignal(
    fromEvent(this.document, 'scroll').pipe(
      map(() => this.document.documentElement.scrollTop > 50),
      distinctUntilChanged(),
    ),
    { initialValue: false },
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
      routerLink: '/login/empresa',
    },
  ];
}
