import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-public-layout',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="flex min-h-screen flex-col bg-slate-50 font-sans">
      <header
        class="z-10 flex h-20 w-full shrink-0 items-center justify-between bg-white px-8 shadow-sm"
      >
        <div class="flex items-center gap-3 text-xl font-bold tracking-tight text-slate-800">
          <div class="flex items-center justify-center rounded-lg bg-indigo-600 p-2">
            <i class="pi pi-heart-fill text-xl text-white"></i>
          </div>
          MedFlow
        </div>
        <div class="flex gap-4">
          <a
            href="/login"
            class="rounded-lg px-5 py-2 font-bold text-indigo-600 transition-colors hover:bg-slate-50"
            >Entrar</a
          >
          <a
            href="/cadastro"
            class="rounded-lg bg-indigo-600 px-5 py-2 font-bold text-white shadow-sm transition-all hover:bg-indigo-700 hover:shadow-md"
            >Cadastrar</a
          >
        </div>
      </header>

      <main class="flex flex-1 flex-col">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
})
export class PublicLayoutComponent {}
