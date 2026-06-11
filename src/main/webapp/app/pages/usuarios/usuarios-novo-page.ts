import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonDirective } from 'primeng/button';
import { DatePicker } from 'primeng/datepicker';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Toast } from 'primeng/toast';
import { firstValueFrom } from 'rxjs';

import type { UsuarioInput } from '../../schemas/usuario.schema';
import { UsuarioService } from '../../services/usuario.service';

interface TipoAcessoOption {
  label: string;
  value: string;
}

@Component({
  selector: 'app-usuarios-novo-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    RouterLink,
    FormsModule,
    DatePipe,
    ButtonDirective,
    InputText,
    DatePicker,
    Select,
    Toast,
  ],
  template: `
    <p-toast />

    <div class="max-w-xl mx-auto">
      <div class="mb-8">
        <h1 class="text-2xl font-bold text-surface-900 tracking-tight">Novo usuário</h1>
        <p class="text-base text-surface-500 mt-1.5">Cadastre um novo usuário no sistema</p>
      </div>

      <div class="bg-white border border-surface-200 rounded-2xl p-8 shadow-sm space-y-5">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Nome</label>
            <input pInputText class="w-full" [(ngModel)]="form.firstName" placeholder="Nome" />
          </div>
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Sobrenome</label>
            <input pInputText class="w-full" [(ngModel)]="form.lastName" placeholder="Sobrenome" />
          </div>
        </div>

        <div class="flex flex-col gap-1.5">
          <label class="text-sm font-semibold text-surface-700">Nome de usuário</label>
          <input pInputText class="w-full" [(ngModel)]="form.username" placeholder="usuario" />
        </div>

        <div class="flex flex-col gap-1.5">
          <label class="text-sm font-semibold text-surface-700">E-mail</label>
          <input
            pInputText
            class="w-full"
            [(ngModel)]="form.email"
            placeholder="email@exemplo.com"
            type="email"
          />
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">CPF</label>
            <input pInputText class="w-full" [(ngModel)]="form.cpf" placeholder="000.000.000-00" />
          </div>
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Telefone</label>
            <input
              pInputText
              class="w-full"
              [(ngModel)]="form.telefone"
              placeholder="(00) 00000-0000"
            />
          </div>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Data de nascimento</label>
            <p-datepicker
              [(ngModel)]="form.dataNascimento"
              dateFormat="dd/mm/yy"
              styleClass="w-full"
            />
          </div>
          <div class="flex flex-col gap-1.5">
            <label class="text-sm font-semibold text-surface-700">Tipo de acesso</label>
            <p-select
              [options]="tiposAcesso"
              [(ngModel)]="form.tipoAcesso"
              optionLabel="label"
              optionValue="value"
              placeholder="Selecione"
              styleClass="w-full"
            />
          </div>
        </div>

        <div class="flex gap-3 pt-4">
          <button pButton label="Cancelar" severity="secondary" routerLink="/usuarios"></button>
          <button
            pButton
            label="Criar usuário"
            icon="pi pi-check"
            (click)="salvar()"
            [loading]="saving()"
          ></button>
        </div>
      </div>
    </div>
  `,
})
export class UsuariosNovoPage {
  private readonly router = inject(Router);
  private readonly usuarioService = inject(UsuarioService);
  private readonly messageService = inject(MessageService);

  readonly saving = signal(false);

  readonly tiposAcesso: TipoAcessoOption[] = [
    { label: 'Usuário', value: 'USUARIO' },
    { label: 'Recepcionista', value: 'RECEPCIONISTA' },
    { label: 'Médico', value: 'MEDICO' },
    { label: 'Administrador', value: 'ADMINISTRADOR' },
  ];

  readonly form = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    cpf: '',
    telefone: '',
    dataNascimento: null as Date | null,
    tipoAcesso: 'USUARIO' as string,
  };

  async salvar(): Promise<void> {
    const f = this.form;
    if (!f.firstName || !f.lastName || !f.username || !f.email || !f.cpf || !f.dataNascimento) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Campos obrigatórios',
        detail: 'Preencha todos os campos obrigatórios.',
      });
      return;
    }

    this.saving.set(true);
    try {
      const input: UsuarioInput = {
        organizacaoId: '', // backend usará org do usuário autenticado ou padrão
        username: f.username,
        email: f.email,
        firstName: f.firstName,
        lastName: f.lastName,
        cpf: f.cpf,
        telefone: f.telefone,
        dataNascimento: f.dataNascimento!.toISOString().split('T')[0],
        tipoAcesso: f.tipoAcesso,
      };
      await firstValueFrom(this.usuarioService.create(input));
      this.messageService.add({
        severity: 'success',
        summary: 'Usuário criado',
        detail: 'Usuário criado com sucesso.',
      });
      setTimeout(() => this.router.navigate(['/usuarios']), 1500);
    } catch (err: any) {
      const msg = err?.error?.detail ?? err?.message ?? 'Erro ao criar usuário.';
      this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
    } finally {
      this.saving.set(false);
    }
  }
}
