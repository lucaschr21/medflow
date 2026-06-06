import { ChangeDetectionStrategy, Component, computed, effect, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { NAV_GROUPS } from '../../@shared/layout/nav-config';

@Component({
  selector: 'app-default-route-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '',
})
export class DefaultRoutePage {
  private readonly authorizationService = inject(AuthorizationService);
  private readonly router = inject(Router);

  private readonly firstVisibleHref = computed(
    () =>
      NAV_GROUPS.flatMap((group) => group.items).find(
        (item) => item.href && (!item.permission || this.authorizationService.can(item.permission)),
      )?.href,
  );

  constructor() {
    effect(() => {
      if (!this.authorizationService.initialized()) {
        return;
      }

      const href = this.firstVisibleHref();
      if (href) {
        void this.router.navigateByUrl(href, { replaceUrl: true });
      }
    });
  }
}
