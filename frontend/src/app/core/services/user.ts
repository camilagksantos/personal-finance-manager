import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UserResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class User {
  private http = inject(HttpClient);

  readonly users = signal<UserResponse[]>([]);
  readonly currentUser = signal<UserResponse | null>(null);
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
      error: (err) => {
        this.error.set('Erro ao carregar utilizadores');
        this.loading.set(false);
      }
    });
  }

  getById(id: number): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<UserResponse>(`${environment.apiUrl}/users/${id}`).subscribe({
      next: (data) => {
        this.currentUser.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Erro ao carregar utilizador');
        this.loading.set(false);
      }
    });
  }

  setCurrentUser(user: UserResponse): void {
    this.currentUser.set(user);
  }
}