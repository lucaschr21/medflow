import { computed, inject, Injectable } from '@angular/core';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import Keycloak, { type KeycloakResourceAccess, type KeycloakTokenParsed } from 'keycloak-js';

const PENDING_EVENTS = new Set<KeycloakEventType>([
  KeycloakEventType.KeycloakAngularNotInitialized,
  KeycloakEventType.KeycloakAngularInit,
]);

@Injectable({ providedIn: 'root' })
export class AuthenticationStore {
  private readonly keycloak = inject(Keycloak);
  private readonly keycloakEvent = inject(KEYCLOAK_EVENT_SIGNAL);

  readonly ready = computed(() => !PENDING_EVENTS.has(this.keycloakEvent().type));

  readonly authenticated = computed(
    () => (this.keycloakEvent(), this.keycloak.authenticated === true),
  );

  readonly token = computed(() => (this.keycloakEvent(), this.keycloak.token ?? null));

  readonly claims = computed<KeycloakTokenParsed | null>(
    () => (
      this.keycloakEvent(),
      this.keycloak.authenticated === true ? (this.keycloak.tokenParsed ?? null) : null
    ),
  );

  readonly id = computed(() => this.claims()?.sub ?? null);

  readonly username = computed<string | null>(() => this.claims()?.['preferred_username'] ?? null);

  readonly email = computed<string | null>(() => this.claims()?.['email'] ?? null);

  readonly fullName = computed<string | null>(() => this.claims()?.['name'] ?? null);

  readonly name = computed<string | null>(() => this.claims()?.['given_name'] ?? null);

  readonly surname = computed<string | null>(() => this.claims()?.['family_name'] ?? null);

  readonly groups = computed<readonly string[]>(() => this.claims()?.['groups'] ?? EMPTY_ARRAY);

  readonly realmRoles = computed<readonly string[]>(
    () => this.claims()?.realm_access?.roles ?? EMPTY_ARRAY,
  );

  readonly resourceRoles = computed<Readonly<KeycloakResourceAccess>>(
    () => this.claims()?.resource_access ?? EMPTY_RESOURCE_ACCESS,
  );

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
}

const EMPTY_ARRAY: readonly string[] = [];
const EMPTY_RESOURCE_ACCESS: Readonly<KeycloakResourceAccess> = {};
