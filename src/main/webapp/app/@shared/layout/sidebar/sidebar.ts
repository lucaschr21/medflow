import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import type { NavGroup } from '../nav-config';

@Component({
  selector: 'app-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {
  readonly groups = input.required<readonly NavGroup[]>();
  readonly collapsed = input(false);

  readonly sidebarClasses = computed(() => ({
    'app-sidebar': true,
    'app-sidebar--collapsed': this.collapsed(),
  }));
}
