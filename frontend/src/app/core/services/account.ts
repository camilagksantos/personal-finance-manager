import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AccountRequest, AccountResponse } from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class Account {
  private http = inject(HttpClient);
  private currentUserId: number | null = null;

  readonly accounts = signal<AccountResponse[]>([]);
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  readonly totalBalance = computed(() =>
    this.accounts().reduce((sum, acc) => sum + acc.balance, 0)
  );

  readonly byType = computed(() => {
    const map: Record<string, AccountResponse[]> = {};
    this.accounts().forEach(acc => {
      if (!map[acc.type]) map[acc.type] = [];
      map[acc.type].push(acc);
    });
    return map;
  });

  loadByUser(userId: number): void {
    this.currentUserId = userId;
    this.loading.set(true);
    this.error.set(null);

    this.http.get<AccountResponse[]>(`${environment.apiUrl}/accounts/user/${userId}`).subscribe({
      next: (data) => {
        if (this.currentUserId !== userId) return;
        this.accounts.set(data);
        this.loading.set(false);
      },
      error: () => {
        if (this.currentUserId !== userId) return;
        this.error.set('Erro ao carregar contas');
        this.loading.set(false);
      }
    });
  }

  create(request: AccountRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.post<AccountResponse>(`${environment.apiUrl}/accounts`, request).subscribe({
      next: (data) => {
        this.accounts.update(accounts => [...accounts, data]);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao criar conta');
        this.loading.set(false);
      }
    });
  }

  update(id: string, request: AccountRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.put<AccountResponse>(`${environment.apiUrl}/accounts/${id}`, request).subscribe({
      next: (data) => {
        this.accounts.update(accounts =>
          accounts.map(acc => acc.id === id ? data : acc)
        );
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao actualizar conta');
        this.loading.set(false);
      }
    });
  }

  delete(id: string): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.delete(`${environment.apiUrl}/accounts/${id}`).subscribe({
      next: () => {
        this.accounts.update(accounts => accounts.filter(acc => acc.id !== id));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao eliminar conta');
        this.loading.set(false);
      }
    });
  }
}