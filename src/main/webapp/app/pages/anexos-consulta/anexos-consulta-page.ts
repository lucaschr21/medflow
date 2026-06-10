import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { FloatLabel } from 'primeng/floatlabel';
import { FileUploadModule } from 'primeng/fileupload';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { formatBytes, orDash, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { AnexoConsulta, AnexoConsultaInput } from '../../schemas/anexo-consulta.schema';
import { AnexoConsultaService } from '../../services/anexo-consulta.service';

@Component({
  selector: 'app-anexos-consulta-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, TextareaModule, FloatLabel, FileUploadModule],
  template: `
    <app-resource-table-page
      title="Anexos de consulta"
      subtitle="Arquivos de exames e documentos vinculados às consultas."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhum anexo encontrado."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      width="540px"
      (visibleChange)="dialogVisible.set($event)"
      (confirm)="saveDialog()"
      (cancel)="closeDialog()"
    >
      <div class="flex flex-col gap-5 pt-2">
        <p-floatlabel variant="on">
          <input pInputText id="consultaId" [(ngModel)]="formConsultaId" style="width:100%" />
          <label for="consultaId">ID da Consulta *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="nomeArquivo" [(ngModel)]="formNomeArquivo" style="width:100%" />
          <label for="nomeArquivo">Nome do arquivo *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="contentType" [(ngModel)]="formContentType" placeholder="ex: application/pdf" style="width:100%" />
          <label for="contentType">Content-Type *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <textarea pTextarea id="descricao" [(ngModel)]="formDescricao" rows="2" style="width:100%"></textarea>
          <label for="descricao">Descrição</label>
        </p-floatlabel>
        @if (dialogMode() === 'create') {
          <div>
            <label class="text-sm text-surface-500 block mb-2">Arquivo *</label>
            <p-fileupload
              mode="basic"
              chooseLabel="Selecionar arquivo"
              [auto]="false"
              (onSelect)="onFileSelect($event)"
            />
          </div>
        }
      </div>
    </app-resource-form-dialog>
  `,
})
export class AnexosConsultaPage extends ResourceCrudPageBase<AnexoConsulta, AnexoConsultaInput> {
  protected readonly service = inject(AnexoConsultaService);
  private readonly authorizationService = inject(AuthorizationService);

  formConsultaId = signal('');
  formNomeArquivo = signal('');
  formContentType = signal('');
  formDescricao = signal('');
  formArquivoBase64 = signal('');
  formTamanhoBytes = signal(0);

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nomeArquivo', header: 'Arquivo' },
    { field: 'contentType', header: 'Tipo' },
    { field: 'tamanhoBytes', header: 'Tamanho' },
    { field: 'consultaId', header: 'Consulta' },
    { field: 'descricao', header: 'Descrição' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['anexo-consulta', 'create']) ? 'Novo anexo' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['anexo-consulta', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((a) => ({
      id: a.id,
      values: {
        nomeArquivo: a.nomeArquivo,
        contentType: a.contentType,
        tamanhoBytes: formatBytes(a.tamanhoBytes),
        consultaId: shortId(a.consultaId),
        descricao: orDash(a.descricao),
      },
    })),
  );

  onFileSelect(event: { files: File[] }): void {
    const file = event.files[0];
    if (!file) return;
    this.formNomeArquivo.set(file.name);
    this.formContentType.set(file.type);
    this.formTamanhoBytes.set(file.size);
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result as string;
      this.formArquivoBase64.set(result.split(',')[1] ?? '');
    };
    reader.readAsDataURL(file);
  }

  protected createTitle() { return 'Novo anexo'; }
  protected editTitle() { return 'Editar informações do anexo'; }

  buildCreateInput(): AnexoConsultaInput {
    return {
      consultaId: this.formConsultaId(),
      nomeArquivo: this.formNomeArquivo(),
      contentType: this.formContentType(),
      tamanhoBytes: this.formTamanhoBytes(),
      descricao: this.formDescricao() || null,
      arquivo: this.formArquivoBase64(),
    };
  }
  buildEditInput(): AnexoConsultaInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((a) => a.id === id);
    if (!e) return;
    this.formConsultaId.set(e.consultaId);
    this.formNomeArquivo.set(e.nomeArquivo);
    this.formContentType.set(e.contentType);
    this.formDescricao.set(e.descricao ?? '');
    this.formTamanhoBytes.set(e.tamanhoBytes);
  }

  override openCreateDialog(): void {
    this.formConsultaId.set(''); this.formNomeArquivo.set(''); this.formContentType.set('');
    this.formDescricao.set(''); this.formArquivoBase64.set(''); this.formTamanhoBytes.set(0);
    super.openCreateDialog();
  }
}
