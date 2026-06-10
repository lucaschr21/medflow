import {
  ChangeDetectionStrategy,
  Component,
  input,
  output,
} from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-resource-form-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DialogModule, ButtonDirective],
  template: `
    <p-dialog
      [header]="title()"
      [visible]="visible()"
      (visibleChange)="visibleChange.emit($event)"
      [modal]="true"
      [style]="{ width: width() }"
      [closable]="!saving()"
      [draggable]="false"
      [resizable]="false"
    >
      <ng-content />

      <ng-template #footer>
        <button
          pButton
          type="button"
          label="Cancelar"
          severity="secondary"
          [disabled]="saving()"
          (click)="cancel.emit()"
        ></button>
        <button
          pButton
          type="button"
          [label]="confirmLabel()"
          [loading]="saving()"
          (click)="confirm.emit()"
        ></button>
      </ng-template>
    </p-dialog>
  `,
})
export class ResourceFormDialog {
  readonly title = input.required<string>();
  readonly visible = input(false);
  readonly saving = input(false);
  readonly confirmLabel = input('Salvar');
  readonly width = input('480px');

  readonly visibleChange = output<boolean>();
  readonly confirm = output();
  readonly cancel = output();
}
