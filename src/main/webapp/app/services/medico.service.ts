import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Medico, MedicoInput } from '../schemas/medico.schema';

@Injectable({ providedIn: 'root' })
export class MedicoService extends ProtectedResourceService<
  'medico',
  'deactivate',
  Medico,
  MedicoInput
> {
  protected readonly resource = 'medico' as const;
  protected readonly resourcePath = 'medicos';
  protected readonly removeScope = 'deactivate' as const;
}
