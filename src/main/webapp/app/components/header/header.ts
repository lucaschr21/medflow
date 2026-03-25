import { DOCUMENT } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import type { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { distinctUntilChanged, fromEvent, map } from 'rxjs';

import { ThemeToggle } from './theme-toggle/theme-toggle';

@Component({
  selector: 'app-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MenubarModule, ThemeToggle],
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
}
