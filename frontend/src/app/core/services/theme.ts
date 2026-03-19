import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Theme {
  readonly currentTheme = signal<'light' | 'dark'>('light');

  constructor() {
    const hour = new Date().getHours();
    const theme = hour >= 6 && hour < 18 ? 'light' : 'dark';
    this.applyTheme(theme);
  }

  toggle(): void {
    const next = this.currentTheme() === 'light' ? 'dark' : 'light';
    this.applyTheme(next);
  }

  private applyTheme(theme: 'light' | 'dark'): void {
    document.body.classList.remove('theme-light', 'theme-dark');
    document.body.classList.add(`theme-${theme}`);
    this.currentTheme.set(theme);
  }
}
