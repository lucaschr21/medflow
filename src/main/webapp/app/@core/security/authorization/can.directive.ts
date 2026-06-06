import { Directive, effect, inject, input, TemplateRef, ViewContainerRef } from '@angular/core';

import { AuthorizationService } from './authorization.service';
import type { PermissionTuple } from './authorization.types';

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

  readonly permission = input.required<PermissionTuple>({
    alias: 'mfCan',
  });

  constructor() {
    effect(() => {
      const allowed = this.authorizationService.can(this.permission());

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
}
