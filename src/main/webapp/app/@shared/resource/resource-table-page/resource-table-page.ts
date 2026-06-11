import { ChangeDetectionStrategy, Component, computed, input, output } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { Tag } from 'primeng/tag';

import { PageHeader } from '../../layout/page-header/page-header';

export interface ResourceTableColumn {
  readonly field: string;
  readonly header: string;
}

export interface ResourceTableRow {
  readonly id: string;
  readonly values: Readonly<Record<string, string>>;
}

export interface ResourceTableAction {
  readonly label: string;
  readonly icon: string;
  readonly severity?:
    | 'primary'
    | 'secondary'
    | 'info'
    | 'success'
    | 'warn'
    | 'danger'
    | 'help'
    | 'contrast';
  readonly visible?: (row: ResourceTableRow) => boolean;
}

@Component({
  selector: 'app-resource-table-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [PageHeader, ButtonDirective, Tag, TableModule],
  templateUrl: './resource-table-page.html',
  styleUrl: './resource-table-page.scss',
})
export class ResourceTablePage {
  readonly title = input.required<string>();
  readonly subtitle = input.required<string>();
  readonly createLabel = input<string | null>(null);
  readonly total = input(0);
  readonly loading = input(false);
  readonly emptyMessage = input('Nenhum registro encontrado.');
  readonly columns = input.required<readonly ResourceTableColumn[]>();
  readonly rows = input.required<readonly ResourceTableRow[]>();
  readonly actions = input<readonly ResourceTableAction[]>([]);

  readonly createClick = output<void>();
  readonly actionClick = output<{ action: string; row: ResourceTableRow }>();
  readonly rowClick = output<ResourceTableRow>();

  readonly tableRows = computed(() => [...this.rows()]);

  readonly hasActions = computed(() => this.actions().length > 0);

  visibleActions(row: ResourceTableRow): ResourceTableAction[] {
    return this.actions().filter((a) => !a.visible || a.visible(row));
  }
}
