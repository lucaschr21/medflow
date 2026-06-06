import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type {
  RegistroAtendimento,
  RegistroAtendimentoInput,
} from '../schemas/registro-atendimento.schema';

@Injectable({ providedIn: 'root' })
export class RegistroAtendimentoService extends ProtectedResourceService<
  'registro-atendimento',
  'delete',
  RegistroAtendimento,
  RegistroAtendimentoInput
> {
  protected readonly resource = 'registro-atendimento' as const;
  protected readonly resourcePath = 'registros-atendimento';
  protected readonly removeScope = 'delete' as const;
}
