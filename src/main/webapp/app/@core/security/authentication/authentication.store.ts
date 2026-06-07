import { computed, inject, Injectable } from '@angular/core';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import Keycloak, { type KeycloakTokenParsed } from 'keycloak-js';

/**
 * Representa os principais dados do usuário autenticado expostos para a UI.
 */
export interface AuthenticationUser {
  readonly id: string | null;
  readonly username: string | null;
  readonly email: string | null;
  readonly name: string | null;
}

const PENDING_EVENTS = new Set<KeycloakEventType>([
  KeycloakEventType.KeycloakAngularNotInitialized,
  KeycloakEventType.KeycloakAngularInit,
]);

/**
 * Store reativa da sessão autenticada no frontend.
 *
 * Ela projeta o estado do `keycloak-angular` em signals simples de consumo
 * pela aplicação, como `ready`, `authenticated`, `user` e `token`.
 */
@Injectable({ providedIn: 'root' })
export class AuthenticationStore {
  private readonly keycloak = inject(Keycloak);
  private readonly keycloakEvent = inject(KEYCLOAK_EVENT_SIGNAL);

  private readonly eventType = computed(() => this.keycloakEvent().type);

  readonly ready = computed(() => !PENDING_EVENTS.has(this.eventType()));

  readonly authenticated = computed(() => {
    this.eventType();
    return this.keycloak.authenticated === true;
  });
  readonly user = computed(() => this.authenticationUser());
  readonly token = computed(() => {
    this.eventType();
    return this.keycloak.token ?? null;
  });

  readonly displayName = computed(() => {
    const user = this.user();
    return user?.name ?? user?.username ?? null;
  });

  login(): Promise<void> {
    return this.keycloak.login({
      redirectUri: location.href,
    });
  }

  logout(): Promise<void> {
    return this.keycloak.logout({
      redirectUri: location.origin,
    });
  }

  openAccount(): Promise<void> {
    return this.keycloak.accountManagement();
  }

  refreshToken(minValidity = 30): Promise<boolean> {
    return this.keycloak.updateToken(minValidity);
  }

  private authenticationUser(): AuthenticationUser | null {
    this.eventType();

    if (!this.keycloak.authenticated) {
      return null;
    }

    const token = this.keycloak.tokenParsed;
    return {
      id: this.readClaim(token, 'sub'),
      username: this.readClaim(token, 'preferred_username') ?? this.keycloak.subject ?? null,
      email: this.readClaim(token, 'email'),
      name: this.readClaim(token, 'name'),
    };
  }

  private readClaim(tokenParsed: KeycloakTokenParsed | undefined, claim: string): string | null {
    const value = tokenParsed?.[claim];
    return typeof value === 'string' && value.trim() ? value : null;
  }
}
