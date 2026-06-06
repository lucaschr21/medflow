import { Directive, effect, inject, input, TemplateRef, ViewContainerRef } from '@angular/core';

import { AuthorizationService } from './authorization.service';
import type { PermissionDescriptor, PermissionTuple } from './authorization.types';

/**
 * Diretiva estrutural para renderização condicional por permissão funcional.
 *
 * @example
 * ```html
 * <button *mfCan="['organizacao', 'create']">
 *   Nova organização
 * </button>
 * ```
 */
@Directive({
  selector: '[mfCan]',
})
export class CanDirective {
  private readonly templateRef = inject(TemplateRef<unknown>);
  private readonly viewContainerRef = inject(ViewContainerRef);
  private readonly authorizationService = inject(AuthorizationService);
  private hasView = false;

  readonly permission = input.required<PermissionDescriptor | PermissionTuple>({
    alias: 'mfCan',
  });

  constructor() {
    effect(() => {
      const allowed = this.authorizationService.can(this.toPermissionDescriptor(this.permission()));

      if (allowed && !this.hasView) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
        this.hasView = true;
        return;
      }

      if (!allowed && this.hasView) {
        this.viewContainerRef.clear();
        this.hasView = false;
      }
    });
  }

  private toPermissionDescriptor(
    permission: PermissionDescriptor | PermissionTuple,
  ): PermissionDescriptor {
    return Array.isArray(permission)
      ? { resource: permission[0], scope: permission[1] }
      : (permission as PermissionDescriptor);
  }
}
