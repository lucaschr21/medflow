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
      </div>
    </div>
  `,
})
export class HomePublicComponent {}
