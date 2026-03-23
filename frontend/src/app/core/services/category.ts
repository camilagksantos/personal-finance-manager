import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CategoryResponse } from '../models/category.model';

@Injectable({
  providedIn: 'root'
})
export class Category {
  private http = inject(HttpClient);

  readonly categories = signal<CategoryResponse[]>([]);
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  private currentUserId: number | null = null;

  loadByUser(userId: number): void {
    this.currentUserId = userId;
    this.loading.set(true);
    this.error.set(null);

    this.http.get<CategoryResponse[]>(`${environment.apiUrl}/categories/user/${userId}`).subscribe({
      next: (data) => {
        if (this.currentUserId !== userId) return;
        this.categories.set(data);
        this.loading.set(false);
      },
      error: () => {
        if (this.currentUserId !== userId) return;
        this.error.set('Erro ao carregar categorias');
        this.loading.set(false);
      }
    });
  }
}