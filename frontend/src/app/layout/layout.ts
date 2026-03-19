import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Layout {
  readonly sidebarOpen = signal<boolean>(true);

  toggle(): void {
    this.sidebarOpen.update(open => !open);
  }
}
