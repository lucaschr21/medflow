import { inject, Injectable } from '@angular/core';

import { AuthorizationStore } from './authorization.store';
import type { PermissionDescriptor } from './authorization.types';

@Injectable({ providedIn: 'root' })
export class AuthorizationService {
  private readonly store = inject(AuthorizationStore);

  readonly loading = this.store.loading;
  readonly initialized = this.store.initialized;
  readonly permissions = this.store.permissions;
  readonly availableResources = this.store.availableResources;

  can(permission: PermissionDescriptor): boolean {
    return this.store.can(permission);
  }

  canAll(permissions: readonly PermissionDescriptor[]): boolean {
    return this.store.canAll(permissions);
  }

  canAny(permissions: readonly PermissionDescriptor[]): boolean {
    return this.store.canAny(permissions);
  }

  reload(): boolean {
    return this.store.reload();
  }
}
