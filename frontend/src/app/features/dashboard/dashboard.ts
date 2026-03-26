import { Component, inject, OnInit, computed, effect } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { Chart, registerables } from 'chart.js';
import { User } from '../../core/services/user';
import { Account } from '../../core/services/account';
import { Transaction } from '../../core/services/transaction';
import { Category } from '../../core/services/category';
import { UserResponse } from '../../core/models/user.model';
import { Theme } from '../../core/services/theme';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatIconModule,
    RouterLink,
    DatePipe,
    BaseChartDirective
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  protected userService = inject(User);
  private accountService = inject(Account);
  private transactionService = inject(Transaction);
  private categoryService = inject(Category);
  private themeService = inject(Theme);

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

  private getChartColors() {
    const isDark = this.themeService.currentTheme() === 'dark';
    return {
      text: isDark ? 'rgba(232, 237, 245, 0.8)' : 'rgba(26, 42, 64, 0.8)',
      grid: isDark ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0.06)'
    };
  }

  protected chartOptions = computed<ChartOptions>(() => {
    const colors = this.getChartColors();
    return {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: {
            color: colors.text,
            font: { size: 12 }
          }
        }
      },
      scales: {
        x: {
          ticks: { color: colors.text },
          grid: { color: colors.grid }
        },
        y: {
          ticks: { color: colors.text },
          grid: { color: colors.grid }
        }
      }
    };
  });

  protected donutOptions = computed<ChartOptions<'doughnut'>>(() => {
    const colors = this.getChartColors();
    return {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            color: colors.text,
            font: { size: 11 },
            padding: 12
          }
        }
      }
    };
  });

  protected barChartData = computed<ChartData<'bar'>>(() => {
    const transactions = this.transactionService.transactions();
    const months: Record<string, { income: number; expenses: number }> = {};

    transactions.forEach(t => {
      const month = t.date.substring(0, 7);
      if (!months[month]) months[month] = { income: 0, expenses: 0 };
      if (t.type === 'INCOME') months[month].income += t.amount;
      else months[month].expenses += t.amount;
    });

    const sorted = Object.keys(months).sort();

    return {
      labels: sorted.map(m => {
        const [year, month] = m.split('-');
        return new Date(+year, +month - 1).toLocaleString('pt-PT', { month: 'short', year: '2-digit' });
      }),
      datasets: [
        {
          label: 'Receitas',
          data: sorted.map(m => months[m].income),
          backgroundColor: 'rgba(74, 222, 128, 0.7)',
          borderRadius: 6
        },
        {
          label: 'Despesas',
          data: sorted.map(m => months[m].expenses),
          backgroundColor: 'rgba(248, 113, 113, 0.7)',
          borderRadius: 6
        }
      ]
    };
  });

  protected donutChartData = computed<ChartData<'doughnut'>>(() => {
    const transactions = this.transactionService.transactions();
    const categories = this.categoryService.categories();
    const categoryMap = new Map(categories.map(c => [c.id, c.name]));
    const totals: Record<string, number> = {};

    transactions
      .filter(t => t.type === 'EXPENSE')
      .forEach(t => {
        const name = categoryMap.get(t.categoryId) ?? t.categoryId;
        totals[name] = (totals[name] ?? 0) + t.amount;
      });

    const labels = Object.keys(totals);
    const data = labels.map(k => totals[k]);

    return {
      labels,
      datasets: [{
        data,
        backgroundColor: [
          'rgba(59, 130, 246, 0.7)',
          'rgba(248, 113, 113, 0.7)',
          'rgba(74, 222, 128, 0.7)',
          'rgba(251, 146, 60, 0.7)',
          'rgba(167, 139, 250, 0.7)',
          'rgba(34, 211, 238, 0.7)'
        ],
        borderWidth: 0
      }]
    };
  });

  protected lineChartData = computed<ChartData<'line'>>(() => {
    const accounts = this.accountService.accounts();
    const transactions = this.transactionService.transactions();
    const months: Record<string, number> = {};

    const baseBalance = accounts.reduce((sum, a) => sum + a.balance, 0);

    transactions.forEach(t => {
      const month = t.date.substring(0, 7);
      if (!months[month]) months[month] = 0;
      if (t.type === 'INCOME') months[month] += t.amount;
      else months[month] -= t.amount;
    });

    const sorted = Object.keys(months).sort();
    let running = baseBalance;
    const values = sorted.map(m => {
      running += months[m];
      return running;
    });

    return {
      labels: sorted.map(m => {
        const [year, month] = m.split('-');
        return new Date(+year, +month - 1).toLocaleString('pt-PT', { month: 'short', year: '2-digit' });
      }),
      datasets: [{
        label: 'Saldo',
        data: values,
        borderColor: 'rgba(96, 165, 250, 0.9)',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        fill: true,
        tension: 0.4,
        pointBackgroundColor: 'rgba(96, 165, 250, 1)',
        pointRadius: 4
      }]
    };
  });

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
      this.categoryService.loadByUser(user.id);
    }
  }

  protected onUserChange(user: UserResponse): void {
    this.userService.setCurrentUser(user);
    this.accountService.accounts.set([]);
    this.transactionService.transactions.set([]);
    this.accountService.loadByUser(user.id);
    this.categoryService.loadByUser(user.id);
  }
}