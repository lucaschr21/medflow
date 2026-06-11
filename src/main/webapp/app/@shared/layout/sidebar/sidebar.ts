import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import type { AuthenticationUser } from '../../../@core/security/authentication/authentication.store';
import type { NavGroup } from '../nav-config';

const ROLE_LABELS: Record<string, string> = {
  ADMINISTRADOR: 'Administrador',
  MEDICO: 'Médico',
  RECEPCIONISTA: 'Recepcionista',
  USUARIO: 'Usuário',
};

@Component({
  selector: 'app-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {
  readonly groups = input.required<readonly NavGroup[]>();
  readonly user = input<AuthenticationUser | null>(null);

  readonly roleLabel = computed(() => {
    const role = this.user()?.role;
    return role ? (ROLE_LABELS[role] ?? role) : null;
  });

  readonly userName = computed(() => {
    const u = this.user();
    if (!u) return null;
    return u.name ?? u.username ?? 'Usuário';
  });
}
