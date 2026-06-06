import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Especialidade, EspecialidadeInput } from '../schemas/especialidade.schema';

@Injectable({ providedIn: 'root' })
export class EspecialidadeService extends ProtectedResourceService<
  'especialidade',
  'deactivate',
  Especialidade,
  EspecialidadeInput
> {
  protected readonly resource = 'especialidade' as const;
  protected readonly resourcePath = 'especialidades';
  protected readonly removeScope = 'deactivate' as const;
}
