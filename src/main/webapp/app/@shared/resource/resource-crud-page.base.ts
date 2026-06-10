import { computed, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';

import type { FindAllParams } from '../../@core/persistence/find-all.params';
import type { PageResult } from '../../@core/persistence/page-result';
import { ProtectedResourceService } from '../../@core/security/authorization/protected-resource.service';
import type { Resource, Scope } from '../../@core/security/authorization/authorization.types';

const EMPTY_PAGE: PageResult<never> = {
  content: [],
  page: 0,
  size: 0,
  totalElements: 0,
  totalPages: 0,
};

export type DialogMode = 'create' | 'edit';

export abstract class ResourceCrudPageBase<Entity, Input> {
  protected abstract readonly service: ProtectedResourceService<
    Resource,
    Extract<Scope, 'delete' | 'deactivate'>,
    Entity,
    Input
  >;

  protected buildFindAllParams(): FindAllParams {
    return { page: 0, size: 50 };
  }

  protected readonly pageResource = rxResource({
    defaultValue: EMPTY_PAGE as PageResult<Entity>,
    stream: () => this.service.findAll(this.buildFindAllParams()),
  });

  readonly loading = this.pageResource.isLoading;
  readonly entities = computed(() => this.pageResource.value().content);
  readonly total = computed(() => this.pageResource.value().totalElements);

  // Dialog state
  readonly dialogVisible = signal(false);
  readonly dialogMode = signal<DialogMode>('create');
  readonly dialogSaving = signal(false);
  readonly editingId = signal<string | null>(null);

  readonly dialogTitle = computed(() =>
    this.dialogMode() === 'create' ? this.createTitle() : this.editTitle(),
  );

  protected abstract createTitle(): string;
  protected abstract editTitle(): string;

  abstract buildCreateInput(): Input;
  abstract buildEditInput(): Input;
  abstract onEditOpen(id: string): void;

  openCreateDialog(): void {
    this.dialogMode.set('create');
    this.editingId.set(null);
    this.dialogVisible.set(true);
  }

  openEditDialog(id: string): void {
    this.dialogMode.set('edit');
    this.editingId.set(id);
    this.onEditOpen(id);
    this.dialogVisible.set(true);
  }

  closeDialog(): void {
    this.dialogVisible.set(false);
    this.dialogSaving.set(false);
  }

  saveDialog(): void {
    if (this.dialogSaving()) return;
    this.dialogSaving.set(true);

    const mode = this.dialogMode();
    const id = this.editingId();

    const obs$ =
      mode === 'create'
        ? this.service.create(this.buildCreateInput())
        : this.service.update(id!, this.buildEditInput());

    obs$.subscribe({
      next: () => {
        this.closeDialog();
        this.pageResource.reload();
      },
      error: () => {
        this.dialogSaving.set(false);
      },
    });
  }

  deleteRow(id: string): void {
    if (!confirm('Tem certeza que deseja excluir este registro?')) return;
    this.service.remove(id).subscribe({
      next: () => this.pageResource.reload(),
    });
  }
}
