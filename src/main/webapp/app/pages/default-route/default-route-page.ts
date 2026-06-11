import { ChangeDetectionStrategy, Component, computed, effect, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from '../../@core/security/authentication/authentication.service';
import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { getNavGroups } from '../../@shared/layout/nav-config';

@Component({
  selector: 'app-default-route-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '',
})
export class DefaultRoutePage {
  private readonly authService = inject(AuthenticationService);
  private readonly authorizationService = inject(AuthorizationService);
  private readonly router = inject(Router);

  private readonly firstVisibleHref = computed(() => {
    const role = this.authService.user()?.role ?? null;
    return getNavGroups(role)
      .flatMap((group) => group.items)
      .find((item) => !item.permission || this.authorizationService.can(item.permission))?.href;
  });

  constructor() {
    effect(() => {
      if (!this.authorizationService.initialized()) return;
      const href = this.firstVisibleHref();
      if (href) {
        void this.router.navigateByUrl(href, { replaceUrl: true });
      }
    });
  }
}
