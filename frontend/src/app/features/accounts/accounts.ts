import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog } from '@angular/material/dialog';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { Account } from '../../core/services/account';
import { User } from '../../core/services/user';
import { AccountResponse, AccountType } from '../../core/models/account.model';
import { AccountFormDialog, AccountFormDialogData } from './account-form-dialog/account-form-dialog';
import { AccountDeleteDialog } from './account-delete-dialog/account-delete-dialog';
import { DatePipe, NgClass } from '@angular/common';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-accounts',
  imports: [
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSortModule,
    MatTooltipModule,
    FormsModule,
    DatePipe,
    NgClass,
    MatProgressBarModule
  ],
  templateUrl: './accounts.html',
  styleUrl: './accounts.scss'
})
export class Accounts implements OnInit {
  private accountService = inject(Account);
  private userService = inject(User);
  private dialog = inject(MatDialog);

  protected searchTerm = signal('');
  protected selectedType = signal<AccountType | ''>('');
  protected sortField = signal<string>('name');
  protected sortDirection = signal<'asc' | 'desc'>('asc');

  protected loading = this.accountService.loading;
  protected error = this.accountService.error;
  protected totalBalance = this.accountService.totalBalance;

  protected readonly accountTypeLabels: Record<AccountType, string> = {
    CHECKING: 'Conta à ordem',
    SAVINGS: 'Poupança',
    CREDIT_CARD: 'Cartão de crédito'
  };

  protected readonly accountTypeColors: Record<AccountType, string> = {
    CHECKING: 'badge--blue',
    SAVINGS: 'badge--green',
    CREDIT_CARD: 'badge--orange'
  };

  protected readonly accountTypes: { value: AccountType | ''; label: string }[] = [
    { value: '', label: 'Todos os tipos' },
    { value: 'CHECKING', label: 'Conta à ordem' },
    { value: 'SAVINGS', label: 'Poupança' },
    { value: 'CREDIT_CARD', label: 'Cartão de crédito' }
  ];

  protected displayedColumns = ['name', 'type', 'balance', 'createdAt', 'actions'];

  protected filteredAccounts = computed(() => {
    let result = this.accountService.accounts();

    const term = this.searchTerm().trim().toLowerCase();
    if (term) {
      result = result.filter(acc =>
        acc.name.toLowerCase().includes(term)
      );
    }

    if (this.selectedType()) {
      result = result.filter(acc => acc.type === this.selectedType());
    }

    return [...result].sort((a, b) => {
      const field = this.sortField() as keyof AccountResponse;
      const dir = this.sortDirection() === 'asc' ? 1 : -1;

      let valueA: any = a[field];
      let valueB: any = b[field];

      if (field === 'createdAt' || field === 'updatedAt') {
        valueA = new Date(valueA as string).getTime();
        valueB = new Date(valueB as string).getTime();
      }

      if (valueA < valueB) return -1 * dir;
      if (valueA > valueB) return 1 * dir;
      return 0;
    });
  });

  ngOnInit(): void {
    const user = this.userService.currentUser();
    if (user) {
      this.accountService.loadByUser(user.id);
    }
  }

  protected onSort(sort: Sort): void {
    this.sortField.set(sort.active);
    this.sortDirection.set(sort.direction as 'asc' | 'desc');
  }

  protected openCreateDialog(): void {
    const user = this.userService.currentUser();
    if (!user) return;

    const dialogRef = this.dialog.open(AccountFormDialog, {
      data: { userId: user.id } satisfies AccountFormDialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.accountService.create(result);
    });
  }

  protected openEditDialog(account: AccountResponse): void {
    const user = this.userService.currentUser();
    if (!user) return;

    const dialogRef = this.dialog.open(AccountFormDialog, {
      data: { account, userId: user.id } satisfies AccountFormDialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.accountService.update(account.id, result);
    });
  }

  protected openDeleteDialog(account: AccountResponse): void {
    const dialogRef = this.dialog.open(AccountDeleteDialog, {
      data: account
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) this.accountService.delete(account.id);
    });
  }

  protected getTypeColor(type: string): string {
    return this.accountTypeColors[type as AccountType] ?? '';
  }

  protected getTypeLabel(type: string): string {
    return this.accountTypeLabels[type as AccountType] ?? type;
  }
}