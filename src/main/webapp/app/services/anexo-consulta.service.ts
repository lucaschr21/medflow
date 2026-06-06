import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { AnexoConsulta, AnexoConsultaInput } from '../schemas/anexo-consulta.schema';

@Injectable({ providedIn: 'root' })
export class AnexoConsultaService extends ProtectedResourceService<
  'anexo-consulta',
  'delete',
  AnexoConsulta,
  AnexoConsultaInput
> {
  protected readonly resource = 'anexo-consulta' as const;
  protected readonly resourcePath = 'anexos-consulta';
  protected readonly removeScope = 'delete' as const;
}
