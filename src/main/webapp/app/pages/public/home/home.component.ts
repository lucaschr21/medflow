import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-public',
  standalone: true,
  imports: [CardModule, CommonModule],
  template: `
    <div
      class="flex flex-1 flex-col items-center bg-gradient-to-b from-slate-50 to-slate-100 p-10 pb-20"
    >
      <div class="mt-10 mb-16 max-w-2xl text-center">
        <h1 class="mb-6 text-5xl leading-tight font-extrabold tracking-tight text-slate-800">
          Encontre a unidade MedFlow ideal para você
        </h1>
        <p class="text-lg text-slate-600"></p>
      </div>

      <div class="grid w-full max-w-6xl grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
        @for (clinica of clinicas; track clinica.id) {
          <p-card
            styleClass="h-full border-none shadow-md hover:shadow-xl transition-shadow duration-300 rounded-xl overflow-hidden cursor-pointer"
          >
            <ng-template pTemplate="header">
              <img [src]="clinica.imagem" [alt]="clinica.titulo" class="h-56 w-full object-cover" />
            </ng-template>

            <div class="flex flex-col gap-3 p-2">
              <div class="flex items-center gap-2 text-sm text-slate-500">
                <span
                  class="cursor-pointer font-semibold text-orange-500 transition-colors hover:underline"
                  >{{ clinica.categoria }}</span
                >
                <span class="text-slate-300">|</span>
                <span>{{ clinica.informacao }}</span>
              </div>
              <h3 class="line-clamp-2 text-xl leading-snug font-bold text-slate-800">
                {{ clinica.titulo }}
              </h3>
            </div>
          </p-card>
        }
      </div>
    </div>
  `,
})
export class HomePublicComponent {
  clinicas = [
    {
      id: 1,
      imagem: 'https://wallpapers.com/images/featured/playboi-carti-o1duxv134a98qd2w.jpg',
      categoria: 'carti',
      informacao: 'carti',
      titulo: 'kdot > ',
    },
    {
      id: 2,
      imagem: 'https://www.nme.com/wp-content/uploads/2021/09/frank-ocean-2021.jpg',
      categoria: 'ocean',
      informacao: 'ivy',
      titulo: 'lc',
    },
    {
      id: 3,
      imagem:
        'https://tse2.mm.bing.net/th/id/OIF.lIQTIm0IHpVY1PO8icV0xw?rs=1&pid=ImgDetMain&o=7&rm=3',
      categoria: 'james',
      informacao: 'blake',
      titulo: 'ye',
    },
  ];
}
