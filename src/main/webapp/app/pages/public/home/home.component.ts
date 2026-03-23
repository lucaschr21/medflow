import { Component } from '@angular/core';

@Component({
  selector: 'app-home-public',
  standalone: true,
  template: `
    <div
      class="flex flex-1 items-center justify-center bg-gradient-to-b from-slate-50 to-slate-100 p-10 text-center"
    >
      <div class="max-w-2xl">
        <h1 class="mb-6 text-5xl leading-tight font-extrabold tracking-tight text-slate-800">
          Consultas médicas online
        </h1>
        <div class="flex justify-center gap-4">
          <button
            class="rounded-xl bg-indigo-600 px-8 py-4 text-lg font-bold text-white transition-all hover:-translate-y-1 hover:bg-indigo-700 hover:shadow-lg"
          >
            Quero me Consultar
          </button>
          <button
            class="rounded-xl border border-slate-200 bg-white px-8 py-4 text-lg font-bold text-slate-700 transition-all hover:-translate-y-1 hover:border-indigo-600 hover:text-indigo-600"
          >
            Sou Médico
          </button>
        </div>
      </div>
    </div>
  `,
})
export class HomePublicComponent {}
