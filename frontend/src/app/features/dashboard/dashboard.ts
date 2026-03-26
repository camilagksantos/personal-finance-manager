import { Component, inject, OnInit, computed, effect } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { User } from '../../core/services/user';
import { Account } from '../../core/services/account';
import { Transaction } from '../../core/services/transaction';
import { UserResponse } from '../../core/models/user.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatIconModule,
    RouterLink,
    DatePipe,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  protected userService = inject(User);
  private accountService = inject(Account);
  private transactionService = inject(Transaction);

  protected greeting = computed(() => {
    const hour = new Date().getHours();
    if (hour >= 6 && hour < 12) return 'Bom dia';
    if (hour >= 12 && hour < 18) return 'Boa tarde';
    return 'Boa noite';
  });

  protected showOverlay = computed(() => !this.userService.currentUser());

  protected kpis = computed(() => {
    const totalBalance = this.accountService.totalBalance();
    const income = this.transactionService.totalIncome();
    const expenses = this.transactionService.totalExpenses();
    const savings = income - expenses;

    return { totalBalance, income, expenses, savings };
  });

  protected recentTransactions = computed(() =>
    this.transactionService.transactions()
      .slice()
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, 10)
  );

  constructor() {
    effect(() => {
      const accounts = this.accountService.accounts();
      if (!accounts.length) return;

      this.transactionService.transactions.set([]);
      accounts.forEach(acc => {
        this.transactionService.loadByAccount(acc.id);
      });
    });
  }

  ngOnInit(): void {
    this.userService.loadAll();

    const user = this.userService.currentUser();
    if (user) {
      this.accountService.accounts.set([]);
      this.transactionService.transactions.set([]);
      this.accountService.loadByUser(user.id);
    }
  }

  protected onUserChange(user: UserResponse): void {
    this.userService.setCurrentUser(user);
    this.accountService.accounts.set([]);
    this.transactionService.transactions.set([]);
    this.accountService.loadByUser(user.id);
  }
}