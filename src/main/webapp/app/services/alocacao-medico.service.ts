import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { AlocacaoMedico, AlocacaoMedicoInput } from '../schemas/alocacao-medico.schema';

@Injectable({ providedIn: 'root' })
export class AlocacaoMedicoService extends ProtectedResourceService<
  'alocacao-medico',
  'deactivate',
  AlocacaoMedico,
  AlocacaoMedicoInput
> {
  protected readonly resource = 'alocacao-medico' as const;
  protected readonly resourcePath = 'alocacoes-medicas';
  protected readonly removeScope = 'deactivate' as const;
}
