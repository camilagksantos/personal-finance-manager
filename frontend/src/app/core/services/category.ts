import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CategoryRequest, CategoryResponse } from '../models/category.model';

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

  create(request: CategoryRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.post<CategoryResponse>(`${environment.apiUrl}/categories`, request).subscribe({
      next: (data) => {
        this.categories.update(c => [...c, data]);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao criar categoria');
        this.loading.set(false);
      }
    });
  }

  update(id: string, request: CategoryRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.put<CategoryResponse>(`${environment.apiUrl}/categories/${id}`, request).subscribe({
      next: (data) => {
        this.categories.update(c => c.map(cat => cat.id === id ? data : cat));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao actualizar categoria');
        this.loading.set(false);
      }
    });
  }

  delete(id: string): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.delete(`${environment.apiUrl}/categories/${id}`).subscribe({
      next: () => {
        this.categories.update(c => c.filter(cat => cat.id !== id));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao eliminar categoria');
        this.loading.set(false);
      }
    });
  }
}