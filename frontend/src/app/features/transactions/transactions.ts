import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { FormsModule } from '@angular/forms';
import { DatePipe, NgClass } from '@angular/common';
import { format } from 'date-fns';
import { Transaction } from '../../core/services/transaction';
import { Account } from '../../core/services/account';
import { Category } from '../../core/services/category';
import { User } from '../../core/services/user';
import { TransactionResponse, TransactionType } from '../../core/models/transaction.model';
import { TransactionFormDialog, TransactionFormDialogData } from './transaction-form-dialog/transaction-form-dialog';
import { TransactionDeleteDialog } from './transaction-delete-dialog/transaction-delete-dialog';

@Component({
  selector: 'app-transactions',
  imports: [
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSortModule,
    MatTooltipModule,
    MatProgressBarModule,
    FormsModule,
    DatePipe,
    NgClass
  ],
  templateUrl: './transactions.html',
  styleUrl: './transactions.scss'
})
export class Transactions implements OnInit {
  private transactionService = inject(Transaction);
  protected accountService = inject(Account);
  protected categoryService = inject(Category);
  private userService = inject(User);
  private dialog = inject(MatDialog);

  protected loading = this.transactionService.loading;
  protected error = this.transactionService.error;
  protected totalIncome = this.transactionService.totalIncome;
  protected totalExpenses = this.transactionService.totalExpenses;

  protected selectedAccountId = signal<string>('');
  protected selectedType = signal<TransactionType | ''>('');
  protected searchDescription = signal<string>('');
  protected startDate = signal<Date | null>(null);
  protected endDate = signal<Date | null>(null);

  protected activeFilters = signal<Set<string>>(new Set());

  protected readonly filterOptions = [
    { key: 'account', label: 'Conta' },
    { key: 'type', label: 'Tipo' },
    { key: 'period', label: 'Período' },
    { key: 'description', label: 'Descrição' }
  ];

  protected readonly transactionTypeLabels: Record<TransactionType, string> = {
    INCOME: 'Receita',
    EXPENSE: 'Despesa'
  };

  protected readonly transactionTypeColors: Record<TransactionType, string> = {
    INCOME: 'badge--green',
    EXPENSE: 'badge--red'
  };

  protected displayedColumns = ['date', 'description', 'account', 'category', 'type', 'amount', 'actions'];

  protected filteredTransactions = computed(() => {
    let result = this.transactionService.transactions();

    if (this.selectedAccountId()) {
      result = result.filter(t => t.accountId === this.selectedAccountId());
    }

    if (this.selectedType()) {
      result = result.filter(t => t.type === this.selectedType());
    }

    const term = this.searchDescription().trim().toLowerCase();
    if (term) {
      result = result.filter(t =>
        t.description?.toLowerCase().includes(term)
      );
    }

    if (this.startDate()) {
      result = result.filter(t => new Date(t.date) >= this.startDate()!);
    }

    if (this.endDate()) {
      result = result.filter(t => new Date(t.date) <= this.endDate()!);
    }

    return result.sort((a, b) =>
      new Date(b.date).getTime() - new Date(a.date).getTime()
    );
  });

  protected accountName = (accountId: string): string => {
    return this.accountService.accounts().find(a => a.id === accountId)?.name ?? accountId;
  };

  protected categoryName = (categoryId: string): string => {
    return this.categoryService.categories().find(c => c.id === categoryId)?.name ?? categoryId;
  };

  ngOnInit(): void {
    const user = this.userService.currentUser();
    if (!user) return;

    this.accountService.loadByUser(user.id);
    this.categoryService.loadByUser(user.id);
  }

  protected toggleFilter(key: string): void {
    this.activeFilters.update(filters => {
      const next = new Set(filters);
      if (next.has(key)) {
        next.delete(key);
        this.resetFilter(key);
      } else {
        next.add(key);
      }
      return next;
    });
  }

  protected isFilterActive(key: string): boolean {
    return this.activeFilters().has(key);
  }

  private resetFilter(key: string): void {
    if (key === 'account') this.selectedAccountId.set('');
    if (key === 'type') this.selectedType.set('');
    if (key === 'description') this.searchDescription.set('');
    if (key === 'period') {
      this.startDate.set(null);
      this.endDate.set(null);
    }
  }

  protected onAccountFilterChange(accountId: string): void {
    this.selectedAccountId.set(accountId);
    if (accountId) {
      this.transactionService.loadByAccount(accountId);
    }
  }

  protected onPeriodSearch(): void {
    const accountId = this.selectedAccountId();
    if (!accountId || !this.startDate() || !this.endDate()) return;

    this.transactionService.loadByAccountAndPeriod(
      accountId,
      format(this.startDate()!, 'yyyy-MM-dd'),
      format(this.endDate()!, 'yyyy-MM-dd')
    );
  }

  protected openCreateDialog(): void {
    const dialogRef = this.dialog.open(TransactionFormDialog, {
      data: {
        accounts: this.accountService.accounts(),
        categories: this.categoryService.categories()
      } satisfies TransactionFormDialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.transactionService.create(result);
    });
  }

  protected openEditDialog(transaction: TransactionResponse): void {
    const dialogRef = this.dialog.open(TransactionFormDialog, {
      data: {
        transaction,
        accounts: this.accountService.accounts(),
        categories: this.categoryService.categories()
      } satisfies TransactionFormDialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.transactionService.update(transaction.id, result);
    });
  }

  protected openDeleteDialog(transaction: TransactionResponse): void {
    const dialogRef = this.dialog.open(TransactionDeleteDialog, {
      data: transaction
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) this.transactionService.delete(transaction.id);
    });
  }

  protected getTypeColor(type: string): string {
    return this.transactionTypeColors[type as TransactionType] ?? '';
  }

  protected getTypeLabel(type: string): string {
    return this.transactionTypeLabels[type as TransactionType] ?? type;
  }
}