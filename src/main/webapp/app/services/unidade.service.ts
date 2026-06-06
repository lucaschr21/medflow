import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Unidade, UnidadeInput } from '../schemas/unidade.schema';

@Injectable({ providedIn: 'root' })
export class UnidadeService extends ProtectedResourceService<
  'unidade',
  'deactivate',
  Unidade,
  UnidadeInput
> {
  protected readonly resource = 'unidade' as const;
  protected readonly resourcePath = 'unidades';
  protected readonly removeScope = 'deactivate' as const;
}
