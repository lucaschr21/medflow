import { inject, Injectable } from '@angular/core';

import type { UserRole } from './authentication.store';
import { AuthenticationStore } from './authentication.store';

export type { UserRole };

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private readonly store = inject(AuthenticationStore);

  readonly ready = this.store.ready;
  readonly authenticated = this.store.authenticated;
  readonly user = this.store.user;
  readonly token = this.store.token;
  readonly displayName = this.store.displayName;

  login(): Promise<void> {
    return this.store.login();
  }

  logout(): Promise<void> {
    return this.store.logout();
  }

  openAccount(): Promise<void> {
    return this.store.openAccount();
  }

  refreshToken(minValidity?: number): Promise<boolean> {
    return this.store.refreshToken(minValidity);
  }
}
