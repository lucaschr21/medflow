import { Component, HostListener, signal } from '@angular/core';
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
        @for (clinica of clinicas.slice(0, visibleCount()); track clinica.id) {
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
  visibleCount = signal(3);

  @HostListener('window:scroll', [])
  onScroll(): void {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 200) {
      if (this.visibleCount() < this.clinicas.length) {
        this.visibleCount.update((val) => val + 3);
      }
    }
  }

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
    {
      id: 4,
      imagem:
        'https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=1000&auto=format&fit=crop',
      categoria: 'pediatria',
      informacao: 'infantil',
      titulo: 'MedFlow Kids',
    },
    {
      id: 5,
      imagem:
        'https://images.unsplash.com/photo-1538108149393-cebb47acddb2?q=80&w=1000&auto=format&fit=crop',
      categoria: 'cardiologia',
      informacao: 'especializada',
      titulo: 'MedFlow Coração',
    },
    {
      id: 6,
      imagem:
        'https://images.unsplash.com/photo-1516062423079-7ca13cdc7f5a?q=80&w=1000&auto=format&fit=crop',
      categoria: 'clínica',
      informacao: 'geral',
      titulo: 'MedFlow Centro',
    },
  ];
}
