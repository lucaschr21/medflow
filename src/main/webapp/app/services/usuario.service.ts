import { Injectable } from '@angular/core';
import { ProtectedResourceService } from '../@core/security/authorization/protected-resource.service';
import type { Usuario, UsuarioInput } from '../schemas/usuario.schema';

@Injectable({ providedIn: 'root' })
export class UsuarioService extends ProtectedResourceService<
  'usuario',
  'deactivate',
  Usuario,
  UsuarioInput
> {
  protected readonly resource = 'usuario' as const;
  protected readonly resourcePath = 'usuarios';
  protected readonly removeScope = 'deactivate' as const;
}
