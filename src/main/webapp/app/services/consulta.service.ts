import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Consulta, ConsultaInput } from '../schemas/consulta.schema';

@Injectable({ providedIn: 'root' })
export class ConsultaService extends ProtectedResourceService<
  'consulta',
  'delete',
  Consulta,
  ConsultaInput
> {
  protected readonly resource = 'consulta' as const;
  protected readonly resourcePath = 'consultas';
  protected readonly removeScope = 'delete' as const;
}
