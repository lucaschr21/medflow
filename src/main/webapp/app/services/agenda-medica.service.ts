import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { AgendaMedica, AgendaMedicaInput } from '../schemas/agenda-medica.schema';

@Injectable({ providedIn: 'root' })
export class AgendaMedicaService extends ProtectedResourceService<
  'agenda-medica',
  'deactivate',
  AgendaMedica,
  AgendaMedicaInput
> {
  protected readonly resource = 'agenda-medica' as const;
  protected readonly resourcePath = 'agendas-medicas';
  protected readonly removeScope = 'deactivate' as const;
}
