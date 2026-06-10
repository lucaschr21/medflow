import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';
import { SelectModule } from 'primeng/select';

import { AuthorizationService } from '../../@core/security/authorization/authorization.service';
import { ResourceCrudPageBase } from '../../@shared/resource/resource-crud-page.base';
import { ResourceFormDialog } from '../../@shared/resource/resource-form-dialog/resource-form-dialog';
import { orDash, shortId } from '../../@shared/resource/resource-formatters';
import {
  ResourceTablePage,
  type ResourceTableColumn,
  type ResourceTableRow,
} from '../../@shared/resource/resource-table-page/resource-table-page';
import type { Unidade, UnidadeInput } from '../../schemas/unidade.schema';
import { UnidadeService } from '../../services/unidade.service';

const UF_OPTIONS = [
  'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG',
  'PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO',
];

@Component({
  selector: 'app-unidades-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ResourceTablePage, ResourceFormDialog, FormsModule, InputTextModule, FloatLabel, SelectModule],
  template: `
    <app-resource-table-page
      title="Unidades"
      subtitle="Cadastro das unidades vinculadas às organizações."
      [createLabel]="createLabel()"
      [deleteLabel]="deleteLabel()"
      [total]="total()"
      [loading]="loading()"
      [columns]="columns"
      [rows]="tableRows()"
      emptyMessage="Nenhuma unidade encontrada."
      (create)="openCreateDialog()"
      (rowEdit)="openEditDialog($event)"
      (rowDelete)="deleteRow($event)"
    />

    <app-resource-form-dialog
      [title]="dialogTitle()"
      [visible]="dialogVisible()"
      [saving]="dialogSaving()"
      width="600px"
      (visibleChange)="dialogVisible.set($event)"
      (confirm)="saveDialog()"
      (cancel)="closeDialog()"
    >
      <div class="flex flex-col gap-5 pt-2">
        <p-floatlabel variant="on">
          <input pInputText id="nome" [(ngModel)]="formNome" style="width:100%" />
          <label for="nome">Nome *</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="telefone" [(ngModel)]="formTelefone" style="width:100%" />
          <label for="telefone">Telefone</label>
        </p-floatlabel>
        <p-floatlabel variant="on">
          <input pInputText id="organizacaoId" [(ngModel)]="formOrganizacaoId" style="width:100%" />
          <label for="organizacaoId">ID da Organização *</label>
        </p-floatlabel>
        <p class="text-sm text-surface-500 -mt-2">Endereço</p>
        <div class="grid grid-cols-2 gap-4">
          <p-floatlabel variant="on">
            <input pInputText id="logradouro" [(ngModel)]="formLogradouro" style="width:100%" />
            <label for="logradouro">Logradouro *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <input pInputText id="numero" [(ngModel)]="formNumero" style="width:100%" />
            <label for="numero">Número *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <input pInputText id="bairro" [(ngModel)]="formBairro" style="width:100%" />
            <label for="bairro">Bairro *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <input pInputText id="cidade" [(ngModel)]="formCidade" style="width:100%" />
            <label for="cidade">Cidade *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <p-select id="uf" [(ngModel)]="formUf" [options]="ufOptions" style="width:100%" />
            <label for="uf">UF *</label>
          </p-floatlabel>
          <p-floatlabel variant="on">
            <input pInputText id="cep" [(ngModel)]="formCep" style="width:100%" />
            <label for="cep">CEP *</label>
          </p-floatlabel>
          <p-floatlabel variant="on" class="col-span-2">
            <input pInputText id="complemento" [(ngModel)]="formComplemento" style="width:100%" />
            <label for="complemento">Complemento</label>
          </p-floatlabel>
        </div>
      </div>
    </app-resource-form-dialog>
  `,
})
export class UnidadesPage extends ResourceCrudPageBase<Unidade, UnidadeInput> {
  protected readonly service = inject(UnidadeService);
  private readonly authorizationService = inject(AuthorizationService);

  readonly ufOptions = UF_OPTIONS;

  formNome = signal('');
  formTelefone = signal('');
  formOrganizacaoId = signal('');
  formLogradouro = signal('');
  formNumero = signal('');
  formBairro = signal('');
  formCidade = signal('');
  formUf = signal('PA');
  formCep = signal('');
  formComplemento = signal('');

  readonly columns: readonly ResourceTableColumn[] = [
    { field: 'nome', header: 'Nome' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'cidade', header: 'Cidade/UF' },
    { field: 'organizacaoId', header: 'Organização' },
  ];

  readonly createLabel = computed(() =>
    this.authorizationService.can(['unidade', 'create']) ? 'Nova unidade' : null,
  );
  readonly deleteLabel = computed(() =>
    this.authorizationService.can(['unidade', 'delete']) ? 'Excluir' : null,
  );

  readonly tableRows = computed<readonly ResourceTableRow[]>(() =>
    this.entities().map((u) => ({
      id: u.id,
      values: {
        nome: u.nome,
        telefone: u.telefone,
        cidade: `${orDash(u.endereco.cidade)} / ${orDash(u.endereco.uf)}`,
        organizacaoId: shortId(u.organizacaoId),
      },
    })),
  );

  protected createTitle() { return 'Nova unidade'; }
  protected editTitle() { return 'Editar unidade'; }

  buildCreateInput(): UnidadeInput {
    return {
      nome: this.formNome(),
      telefone: this.formTelefone(),
      organizacaoId: this.formOrganizacaoId(),
      endereco: {
        logradouro: this.formLogradouro(),
        numero: this.formNumero(),
        bairro: this.formBairro(),
        cidade: this.formCidade(),
        uf: this.formUf() as any,
        cep: this.formCep(),
        complemento: this.formComplemento() || null,
      },
    };
  }
  buildEditInput(): UnidadeInput { return this.buildCreateInput(); }

  onEditOpen(id: string): void {
    const e = this.entities().find((u) => u.id === id);
    if (!e) return;
    this.formNome.set(e.nome);
    this.formTelefone.set(e.telefone);
    this.formOrganizacaoId.set(e.organizacaoId);
    this.formLogradouro.set(e.endereco.logradouro);
    this.formNumero.set(e.endereco.numero);
    this.formBairro.set(e.endereco.bairro);
    this.formCidade.set(e.endereco.cidade);
    this.formUf.set(e.endereco.uf);
    this.formCep.set(e.endereco.cep);
    this.formComplemento.set(e.endereco.complemento ?? '');
  }

  override openCreateDialog(): void {
    this.formNome.set(''); this.formTelefone.set(''); this.formOrganizacaoId.set('');
    this.formLogradouro.set(''); this.formNumero.set(''); this.formBairro.set('');
    this.formCidade.set(''); this.formUf.set('PA'); this.formCep.set(''); this.formComplemento.set('');
    super.openCreateDialog();
  }
}
