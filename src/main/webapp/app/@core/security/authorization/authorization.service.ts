import { inject, Injectable } from '@angular/core';

import { AuthorizationError } from './authorization.error';
import { AuthorizationStore } from './authorization.store';
import type { PermissionTuple } from './authorization.types';

@Injectable({ providedIn: 'root' })
export class AuthorizationService {
  private readonly store = inject(AuthorizationStore);

  readonly loading = this.store.loading;
  readonly initialized = this.store.initialized;
  readonly loaded = this.store.loaded;
  readonly permissions = this.store.permissions;
  readonly availableResources = this.store.availableResources;

  can(permission: PermissionTuple): boolean {
    return this.store.can(permission);
  }

  canAll(permissions: readonly PermissionTuple[]): boolean {
    return this.store.canAll(permissions);
  }

  canAny(permissions: readonly PermissionTuple[]): boolean {
    return this.store.canAny(permissions);
  }

  reload(): boolean {
    return this.store.reload();
  }

  ensureAllowed(permission: PermissionTuple): void {
    if (!this.can(permission)) {
      throw new AuthorizationError(permission);
    }
  }
}
