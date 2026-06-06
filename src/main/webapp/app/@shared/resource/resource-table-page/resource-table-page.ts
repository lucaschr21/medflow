import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';
import { TableModule } from 'primeng/table';

import { PageHeader } from '../../layout/page-header/page-header';

export interface ResourceTableColumn {
  readonly field: string;
  readonly header: string;
}

export interface ResourceTableRow {
  readonly id: string;
  readonly values: Readonly<Record<string, string>>;
}

@Component({
  selector: 'app-resource-table-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [PageHeader, ButtonDirective, Card, Tag, TableModule],
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
  readonly tableRows = computed(() => [...this.rows()]);
}
