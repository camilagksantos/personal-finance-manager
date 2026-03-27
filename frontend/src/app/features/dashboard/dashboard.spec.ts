import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Dashboard } from './dashboard';
import { Account } from '../../core/services/account';
import { Transaction } from '../../core/services/transaction';
import { AccountResponse } from '../../core/models/account.model';
import { TransactionResponse } from '../../core/models/transaction.model';

HTMLCanvasElement.prototype.getContext = vi.fn() as any;

const mockAccounts: AccountResponse[] = [
  { id: 'uuid-1', userId: 1, name: 'Conta Principal', type: 'CHECKING', balance: 1500, createdAt: '', updatedAt: '' }
];

const mockTransactions: TransactionResponse[] = [
  { id: 't-1', accountId: 'uuid-1', categoryId: 'c-1', amount: 3000, type: 'INCOME', date: '2026-01-01', createdAt: '', updatedAt: '' },
  { id: 't-2', accountId: 'uuid-1', categoryId: 'c-2', amount: 1000, type: 'EXPENSE', date: '2026-01-15', createdAt: '', updatedAt: '' }
];

describe('Dashboard', () => {
  let accountService: Account;
  let transactionService: Transaction;

  beforeEach(async () => {
    localStorage.clear();
    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    accountService = TestBed.inject(Account);
    transactionService = TestBed.inject(Transaction);
  });

  afterEach(() => localStorage.clear());

  it('should create', () => {
    const fixture = TestBed.createComponent(Dashboard);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should compute KPIs correctly from real signals', () => {
    accountService.accounts.set(mockAccounts);
    transactionService.transactions.set(mockTransactions);

    const fixture = TestBed.createComponent(Dashboard);
    const component = fixture.componentInstance as any;

    expect(component.kpis().totalBalance).toBe(1500);
    expect(component.kpis().income).toBe(3000);
    expect(component.kpis().expenses).toBe(1000);
    expect(component.kpis().savings).toBe(2000);
  });

  it('should show overlay when no user is selected', () => {
    const fixture = TestBed.createComponent(Dashboard);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.user-overlay')).toBeTruthy();
  });

  it('should return last 10 transactions sorted by date desc', () => {
    const many: TransactionResponse[] = Array.from({ length: 15 }, (_, i) => ({
      id: `t-${i}`,
      accountId: 'uuid-1',
      categoryId: 'c-1',
      amount: 100,
      type: 'INCOME' as const,
      date: `2026-01-${String(i + 1).padStart(2, '0')}`,
      createdAt: '',
      updatedAt: ''
    }));

    transactionService.transactions.set(many);
    const fixture = TestBed.createComponent(Dashboard);
    const component = fixture.componentInstance as any;

    const recent = component.recentTransactions();
    expect(recent.length).toBe(10);
    expect(recent[0].date).toBe('2026-01-15');
  });
});