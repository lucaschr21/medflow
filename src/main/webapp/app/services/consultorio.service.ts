import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Consultorio, ConsultorioInput } from '../schemas/consultorio.schema';

@Injectable({ providedIn: 'root' })
export class ConsultorioService extends ProtectedResourceService<
  'consultorio',
  'deactivate',
  Consultorio,
  ConsultorioInput
> {
  protected readonly resource = 'consultorio' as const;
  protected readonly resourcePath = 'consultorios';
  protected readonly removeScope = 'deactivate' as const;
}
