import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { BloqueioAgenda, BloqueioAgendaInput } from '../schemas/bloqueio-agenda.schema';

@Injectable({ providedIn: 'root' })
export class BloqueioAgendaService extends ProtectedResourceService<
  'bloqueio-agenda',
  'delete',
  BloqueioAgenda,
  BloqueioAgendaInput
> {
  protected readonly resource = 'bloqueio-agenda' as const;
  protected readonly resourcePath = 'bloqueios-agenda';
  protected readonly removeScope = 'delete' as const;
}
