import { Component, ElementRef, HostListener, signal, inject } from '@angular/core';
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
            href="/cadastro"
            class="flex items-center justify-center rounded-lg rounded-br-2xl bg-indigo-600 px-6 py-2 text-sm font-bold text-white shadow-sm transition-all hover:bg-indigo-700 hover:shadow-md"
          >
            Seja cliente
          </a>

          <div class="relative">
            <button
              (click)="toggleMenu()"
              class="flex items-center gap-2 rounded-lg border-2 border-indigo-600 bg-white px-6 py-2 text-sm font-bold text-indigo-600 shadow-sm transition-colors hover:bg-indigo-50"
            >
              Acesse sua área
              <i
                class="pi pi-angle-down font-bold transition-transform"
                [class.rotate-180]="isMenuOpen()"
                style="font-size: 0.7rem"
              ></i>
            </button>

            @if (isMenuOpen()) {
              <div
                class="ring-opacity-5 absolute right-0 mt-2 w-48 origin-top-right rounded-xl bg-white p-2 shadow-lg ring-1 ring-black focus:outline-none"
              >
                <a
                  href="/login"
                  class="flex items-center gap-3 rounded-lg px-4 py-2.5 text-sm font-medium text-slate-700 transition-colors hover:bg-indigo-50 hover:text-indigo-600"
                >
                  <i class="pi pi-user text-indigo-500"></i>
                  Sou Administrador
                </a>
                <a
                  href="/login-profissional"
                  class="flex items-center gap-3 rounded-lg px-4 py-2.5 text-sm font-medium text-slate-700 transition-colors hover:bg-indigo-50 hover:text-indigo-600"
                >
                  <i class="pi pi-briefcase text-indigo-500"></i>
                  Sou Médico
                </a>
              </div>
            }
          </div>
        </div>
      </header>

      <main class="flex flex-1 flex-col">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
})
export class PublicLayoutComponent {
  isMenuOpen = signal(false);

  private elementRef = inject(ElementRef);

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const targetElement = event.target as HTMLElement;
    if (targetElement && !this.elementRef.nativeElement.contains(targetElement)) {
      this.isMenuOpen.set(false);
    }
  }
  toggleMenu() {
    this.isMenuOpen.update((v) => !v);
  }
}
