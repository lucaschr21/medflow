import { computed, inject, Injectable } from '@angular/core';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import Keycloak from 'keycloak-js';

/**
 * Papéis de experiência do Medflow derivados dos grupos do Keycloak.
 */
export type UserRole = 'ADMINISTRADOR' | 'MEDICO' | 'RECEPCIONISTA' | 'USUARIO';

const ROLE_PRIORITY: readonly UserRole[] = ['ADMINISTRADOR', 'MEDICO', 'RECEPCIONISTA', 'USUARIO'];

const GROUP_TO_ROLE: Record<string, UserRole> = {
  '/ADMINISTRADORES': 'ADMINISTRADOR',
  '/MEDICOS': 'MEDICO',
  '/RECEPCIONISTAS': 'RECEPCIONISTA',
  '/USUARIOS': 'USUARIO',
};

/**
 * Representa os principais dados do usuário autenticado expostos para a UI.
 */
export interface AuthenticationUser {
  readonly id: string | null;
  readonly username: string | null;
  readonly email: string | null;
  readonly name: string | null;
  readonly role: UserRole | null;
  readonly groups: readonly string[];
}

const KEYCLOAK_PENDING_EVENTS = new Set<KeycloakEventType>([
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

  readonly ready = computed(() => !KEYCLOAK_PENDING_EVENTS.has(this.eventType()));
  readonly authenticated = computed(() => {
    this.eventType();
    return this.keycloak.authenticated ?? false;
  });
  readonly user = computed(() => this.authenticationUser());
  readonly token = computed(() => {
    this.eventType();
    return this.keycloak.token ?? null;
  });
  /**
   * Nome preferencial para exibição na UI.
   *
   * Prioriza `name` e usa `username` como fallback.
   */
  readonly displayName = computed(() => {
    const user = this.user();
    return user?.name ?? user?.username ?? null;
  });

  /**
   * Inicia o fluxo de login do Keycloak.
   */
  async login(): Promise<void> {
    await this.keycloak.login({
      redirectUri: location.href,
    });
  }

  async logout(): Promise<void> {
    await this.keycloak.logout({
      redirectUri: location.origin,
    });
  }

  async openAccount(): Promise<void> {
    await this.keycloak.accountManagement();
  }

  async refreshToken(minValidity = 30): Promise<boolean> {
    return this.keycloak.updateToken(minValidity);
  }

  private authenticationUser(): AuthenticationUser | null {
    this.eventType();

    if (!this.keycloak.authenticated) {
      return null;
    }

    const tokenParsed = this.keycloak.tokenParsed;
    const groups = this.readGroups(tokenParsed);
    return {
      id: this.readClaim(tokenParsed, 'sub'),
      username: this.readClaim(tokenParsed, 'preferred_username') ?? this.keycloak.subject ?? null,
      email: this.readClaim(tokenParsed, 'email'),
      name: this.readClaim(tokenParsed, 'name'),
      role: this.resolveRole(groups),
      groups,
    };
  }

  private resolveRole(groups: readonly string[]): UserRole | null {
    for (const role of ROLE_PRIORITY) {
      const groupPath = Object.entries(GROUP_TO_ROLE).find(([, r]) => r === role)?.[0];
      if (groupPath && groups.includes(groupPath)) {
        return role;
      }
    }
    return null;
  }

  private readGroups(tokenParsed: Keycloak.KeycloakTokenParsed | undefined): readonly string[] {
    const groups = tokenParsed?.['groups'];
    if (Array.isArray(groups)) {
      return groups.filter((g): g is string => typeof g === 'string');
    }
    return [];
  }

  private readClaim(
    tokenParsed: Keycloak.KeycloakTokenParsed | undefined,
    claim: string,
  ): string | null {
    const value = tokenParsed?.[claim];
    return typeof value === 'string' && value.trim() ? value : null;
  }
}
