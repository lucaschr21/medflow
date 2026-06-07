import { inject, Injectable } from '@angular/core';

import { AuthenticationStore } from './authentication.store';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private readonly store = inject(AuthenticationStore);

  readonly ready = this.store.ready;
  readonly authenticated = this.store.authenticated;

  readonly token = this.store.token;
  readonly claims = this.store.claims;

  readonly id = this.store.id;
  readonly username = this.store.username;
  readonly email = this.store.email;
  readonly fullName = this.store.fullName;
  readonly name = this.store.name;
  readonly surname = this.store.surname;
  readonly groups = this.store.groups;
  readonly realmRoles = this.store.realmRoles;
  readonly resourceRoles = this.store.resourceRoles;

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
