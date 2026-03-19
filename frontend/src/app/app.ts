import { Component, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterOutlet } from "../../node_modules/@angular/router/types/_router_module-chunk";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MatButtonModule, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  currentTheme = signal<'light' | 'dark'>('light');

  ngOnInit(): void {
    const hour = new Date().getHours();
    const theme = hour >= 6 && hour < 18 ? 'light' : 'dark';
    this.applyTheme(theme);
  }

  toggleTheme(): void {
    const next = this.currentTheme() === 'light' ? 'dark' : 'light';
    this.applyTheme(next);
  }

  private applyTheme(theme: 'light' | 'dark'): void {
    document.body.classList.remove('theme-light', 'theme-dark');
    document.body.classList.add(`theme-${theme}`);
    this.currentTheme.set(theme);
  }
}