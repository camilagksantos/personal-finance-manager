import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { TransactionRequest, TransactionResponse } from '../models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class Transaction {
  private http = inject(HttpClient);

  readonly transactions = signal<TransactionResponse[]>([]);
  readonly loading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  readonly totalIncome = computed(() =>
    this.transactions()
      .filter(t => t.type === 'INCOME')
      .reduce((sum, t) => sum + t.amount, 0)
  );

  readonly totalExpenses = computed(() =>
    this.transactions()
      .filter(t => t.type === 'EXPENSE')
      .reduce((sum, t) => sum + t.amount, 0)
  );

  loadByAccount(accountId: string): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<TransactionResponse[]>(`${environment.apiUrl}/transactions/account/${accountId}`).subscribe({
      next: (data) => {
        this.transactions.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao carregar transacções');
        this.loading.set(false);
      }
    });
  }

  loadByAccountAndPeriod(accountId: string, startDate: string, endDate: string): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<TransactionResponse[]>(
      `${environment.apiUrl}/transactions/account/${accountId}/period`,
      { params: { startDate, endDate } }
    ).subscribe({
      next: (data) => {
        this.transactions.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao carregar transacções');
        this.loading.set(false);
      }
    });
  }

  create(request: TransactionRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.post<TransactionResponse>(`${environment.apiUrl}/transactions`, request).subscribe({
      next: (data) => {
        this.transactions.update(t => [data, ...t]);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao criar transacção');
        this.loading.set(false);
      }
    });
  }

  update(id: string, request: TransactionRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.put<TransactionResponse>(`${environment.apiUrl}/transactions/${id}`, request).subscribe({
      next: (data) => {
        this.transactions.update(t => t.map(tx => tx.id === id ? data : tx));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao actualizar transacção');
        this.loading.set(false);
      }
    });
  }

  delete(id: string): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.delete(`${environment.apiUrl}/transactions/${id}`).subscribe({
      next: () => {
        this.transactions.update(t => t.filter(tx => tx.id !== id));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erro ao eliminar transacção');
        this.loading.set(false);
      }
    });
  }
}