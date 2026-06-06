import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Organizacao, OrganizacaoInput } from '../schemas/organizacao.schema';

/**
 * Service HTTP do recurso de organizações.
 *
 * A implementação expõe o recurso de organizações usando a base protegida do
 * core.
 */
@Injectable({ providedIn: 'root' })
export class OrganizacaoService extends ProtectedResourceService<
  'organizacao',
  'deactivate',
  Organizacao,
  OrganizacaoInput
> {
  protected readonly resource = 'organizacao' as const;
  protected readonly resourcePath = 'organizacoes';
  protected readonly removeScope = 'deactivate' as const;
}
