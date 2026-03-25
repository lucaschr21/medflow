import { DOCUMENT } from '@angular/common';
import { Injectable, inject, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private readonly document = inject(DOCUMENT);
  private readonly storageKey = 'medflow-theme';

  private readonly isDarkModeState = signal(this.initializeTheme());
  readonly isDarkMode = this.isDarkModeState.asReadonly();

  toggleTheme(): void {
    const nextTheme = !this.isDarkModeState();
    this.isDarkModeState.set(nextTheme);
    this.applyTheme(nextTheme);
  }

  private initializeTheme(): boolean {
    const savedTheme = localStorage.getItem(this.storageKey);

    const darkMode =
      savedTheme === 'dark'
        ? true
        : savedTheme === 'light'
          ? false
          : window.matchMedia('(prefers-color-scheme: dark)').matches;

    this.applyTheme(darkMode);
    return darkMode;
  }

  private applyTheme(darkMode: boolean): void {
    this.document.documentElement.classList.toggle('app-dark', darkMode);
    localStorage.setItem(this.storageKey, darkMode ? 'dark' : 'light');
  }
}
