import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UserResponse } from '../models/user.model';

const STORAGE_KEY = 'finance_current_user';

@Injectable({
  providedIn: 'root'
})
export class User {
  private http = inject(HttpClient);

  readonly users = signal<UserResponse[]>([]);
  readonly currentUser = signal<UserResponse | null>(this.loadFromStorage());
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  loadAll(): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<UserResponse[]>(`${environment.apiUrl}/users`).subscribe({
      next: (data) => {
        this.users.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao carregar utilizadores');
        this.loading.set(false);
      }
    });
  }

  setCurrentUser(user: UserResponse): void {
    this.currentUser.set(user);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
  }

  clearCurrentUser(): void {
    this.currentUser.set(null);
    localStorage.removeItem(STORAGE_KEY);
  }

  private loadFromStorage(): UserResponse | null {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  }
}