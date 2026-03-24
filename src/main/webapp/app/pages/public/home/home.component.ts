import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-home-public',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, CardModule, ButtonModule, InputTextModule],
  template: `
    <div class="min-h-screen bg-slate-50">
      <section
        class="relative overflow-hidden rounded-b-[3rem] bg-gradient-to-b from-[#4f46e5] to-[#4338ca] px-6 py-24 text-white shadow-xl"
      >
        <div class="relative z-10 mx-auto max-w-4xl text-center">
          <h1 class="mb-6 text-4xl font-extrabold tracking-tight md:text-6xl">
            Sua saúde em boas mãos, de forma simples e rápida
          </h1>
          <p class="mx-auto mb-10 max-w-2xl text-lg text-indigo-100 md:text-xl">
            Encontre os melhores especialistas, agende consultas e tenha acesso a toda a rede
            MedFlow.
          </p>
        </div>
      </section>

      <section class="mx-auto flex max-w-6xl flex-col items-center px-6 py-20">
        <div class="mb-12 text-center">
          <h2 class="text-3xl font-extrabold tracking-tight text-slate-800 md:text-4xl">
            Nossas Unidades
          </h2>
        </div>

        <div class="grid w-full grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
          @for (clinica of clinicas; track clinica.id) {
            <p-card
              styleClass="h-full border border-slate-100 shadow-lg hover:shadow-2xl transition-all duration-300 rounded-2xl overflow-hidden flex flex-col"
            >
              <ng-template pTemplate="header">
                <div class="group relative h-56 cursor-pointer overflow-hidden">
                  <img
                    [src]="clinica.imagem"
                    [alt]="clinica.titulo"
                    class="h-full w-full object-cover transition-transform duration-500 group-hover:scale-110"
                  />
                  <div
                    class="absolute top-4 left-4 rounded-full bg-white/95 px-3 py-1 text-xs font-bold text-[#4f46e5] shadow-sm backdrop-blur-sm"
                  >
                    {{ clinica.categoria | uppercase }}
                  </div>
                </div>
              </ng-template>
              <div class="flex flex-1 flex-col p-2">
                <h3 class="mb-2 line-clamp-2 text-2xl font-bold text-slate-800">
                  {{ clinica.titulo }}
                </h3>
                <p class="mb-6 flex items-center gap-2 text-slate-500">
                  <svg
                    class="h-4 w-4 shrink-0 text-slate-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                    ></path>
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                    ></path>
                  </svg>
                  <span>{{ clinica.informacao }}</span>
                </p>
              </div>
            </p-card>
          }
        </div>
      </section>
    </div>
  `,
})
export class HomePublicComponent {
  searchQuery = '';

  //teste
  clinicas = [
    {
      id: 1,
      imagem:
        'https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clinica',
      informacao: 'belem',
      titulo: 'clinica',
    },
    {
      id: 2,
      imagem:
        'https://images.unsplash.com/photo-1538108149393-cebb47acddb2?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clinica',
      informacao: 'belem',
      titulo: 'clinica',
    },
    {
      id: 3,
      imagem:
        'https://images.unsplash.com/photo-1516062423079-7ca13cdc7f5a?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clinica',
      informacao: 'belem',
      titulo: 'clinica',
    },
    {
      id: 4,
      imagem:
        'https://images.unsplash.com/photo-1586773860418-d37222d8fce3?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clinica',
      informacao: 'belem',
      titulo: 'clinica',
    },
    {
      id: 5,
      imagem:
        'https://images.unsplash.com/photo-1504439468489-c8920d786a2b?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clinica',
      informacao: 'belem',
      titulo: 'clinica',
    },
    {
      id: 6,
      imagem:
        'https://images.unsplash.com/photo-1551076805-e1869033e561?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clinica',
      informacao: 'belem',
      titulo: 'clinica',
    },
  ];
}
