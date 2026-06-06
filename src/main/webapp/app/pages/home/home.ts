import { JsonPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { firstValueFrom } from 'rxjs';

import { ErrorNotifierService } from '../../@core/handler/error-notifier.service';
import { CanDirective } from '../../@core/security/authorization/can.directive';
import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { AuthenticationService } from '../../@core/security/authentication/authentication.service';
import type { Organizacao } from '../../schemas/organizacao.schema';
import type { OrganizacaoInput } from '../../schemas/organizacao.schema';
import { OrganizacaoService } from '../../services/organizacao.service';

@Component({
  selector: 'app-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ButtonDirective, CanDirective, JsonPipe],
  templateUrl: './home.html',
})
export class Home {
  private readonly authenticationService = inject(AuthenticationService);
  private readonly authorizationService = inject(AuthorizationService);
  private readonly errorNotifier = inject(ErrorNotifierService);

  protected readonly organizacaoService = inject(OrganizacaoService);

  readonly authenticated = this.authenticationService.authenticated;
  readonly displayName = this.authenticationService.displayName;
  readonly permissionsInitialized = this.authorizationService.initialized;
  readonly permissionsLoaded = this.authorizationService.loaded;
  readonly availableResources = this.authorizationService.availableResources;

  readonly loadingOrganizacoes = signal(false);
  readonly submittingOrganizacao = signal(false);
  readonly removingOrganizacao = signal(false);
  readonly organizacoes = signal<readonly Organizacao[]>([]);
  readonly lastAction = signal<string | null>(null);
  readonly selectedOrganizacao = computed(() => this.organizacoes()[0] ?? null);

  readonly canReadOrganizacao = computed(() =>
    this.authorizationService.can(['organizacao', 'read']),
  );
  readonly canCreateOrganizacao = computed(() =>
    this.authorizationService.can(['organizacao', 'create']),
  );
  readonly canDeactivateOrganizacao = computed(() =>
    this.authorizationService.can(['organizacao', 'deactivate']),
  );

  async loadOrganizacoes(): Promise<void> {
    this.loadingOrganizacoes.set(true);

    try {
      const page = await firstValueFrom(this.organizacaoService.findAll({ page: 0, size: 5 }));
      this.organizacoes.set(page.content);
      this.lastAction.set(`Leitura concluída: ${page.content.length} organizações carregadas.`);
    } catch (error) {
      this.handleActionError(error, 'Leitura de organizações bloqueada ou falhou.');
    } finally {
      this.loadingOrganizacoes.set(false);
    }
  }

  async createOrganizacao(): Promise<void> {
    this.submittingOrganizacao.set(true);

    try {
      const organizacao = await firstValueFrom(
        this.organizacaoService.create(this.buildOrganizacaoInput()),
      );
      this.organizacoes.update((current) => [organizacao, ...current]);
      this.lastAction.set(`Criação concluída: ${organizacao.nome}.`);
    } catch (error) {
      this.handleActionError(error, 'Criação de organização bloqueada ou falhou.');
    } finally {
      this.submittingOrganizacao.set(false);
    }
  }

  async removeFirstOrganizacao(): Promise<void> {
    const organizacao = this.selectedOrganizacao();
    if (!organizacao) {
      this.lastAction.set('Nenhuma organização carregada para remoção.');
      return;
    }

    this.removingOrganizacao.set(true);

    try {
      await firstValueFrom(this.organizacaoService.remove(organizacao.id));
      this.organizacoes.update((current) =>
        current.filter((currentOrganizacao) => currentOrganizacao.id !== organizacao.id),
      );
      this.lastAction.set(`Remoção concluída: ${organizacao.nome}.`);
    } catch (error) {
      this.handleActionError(error, 'Remoção de organização bloqueada ou falhou.');
    } finally {
      this.removingOrganizacao.set(false);
    }
  }

  reloadPermissions(): void {
    this.authorizationService.reload();
  }

  login(): Promise<void> {
    return this.authenticationService.login();
  }

  logout(): Promise<void> {
    return this.authenticationService.logout();
  }

  private buildOrganizacaoInput(): OrganizacaoInput {
    const suffix = `${Date.now()}`;

    return {
      nome: `Organização Sandbox ${suffix}`,
      email: `organizacao.sandbox.${suffix}@gmail.com`,
      telefone: '91999999999',
      logotipo: null,
      logotipoContentType: null,
      corPrimaria: '#1E88E5',
    };
  }

  private handleActionError(error: unknown, fallbackMessage: string): void {
    this.lastAction.set(error instanceof Error ? error.message : fallbackMessage);
    this.errorNotifier.notifyUnexpected(error);
  }
}
