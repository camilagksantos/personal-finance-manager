import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Account } from './account';
import { AccountResponse } from '../models/account.model';

const mockAccounts: AccountResponse[] = [
  { id: 'uuid-1', userId: 1, name: 'Conta Principal', type: 'CHECKING', balance: 1500, createdAt: '2026-01-01T00:00:00', updatedAt: '2026-01-01T00:00:00' },
  { id: 'uuid-2', userId: 1, name: 'Poupança', type: 'SAVINGS', balance: 3000, createdAt: '2026-01-01T00:00:00', updatedAt: '2026-01-01T00:00:00' }
];

describe('Account', () => {
  let service: Account;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(Account);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('should initialise with empty accounts', () => {
    expect(service.accounts()).toEqual([]);
  });

  it('should compute totalBalance correctly', () => {
    service.accounts.set(mockAccounts);
    expect(service.totalBalance()).toBe(4500);
  });

  it('should load accounts by user', () => {
    service.loadByUser(1);
    const req = httpTesting.expectOne('http://localhost:8080/api/v1/accounts/user/1');
    req.flush(mockAccounts);

    expect(service.accounts().length).toBe(2);
    expect(service.loading()).toBe(false);
  });

  it('should discard stale response when userId changes (race condition guard)', () => {
    service.loadByUser(1);
    service.loadByUser(2);

    const req2 = httpTesting.expectOne('http://localhost:8080/api/v1/accounts/user/2');
    const req1 = httpTesting.expectOne('http://localhost:8080/api/v1/accounts/user/1');

    req2.flush(mockAccounts);
    req1.flush([{ ...mockAccounts[0], name: 'Stale Account' }]);

    expect(service.accounts().length).toBe(2);
    expect(service.accounts()[0].name).toBe('Conta Principal');
  });

  it('should add new account after create', () => {
    service.accounts.set(mockAccounts);
    service.create({ userId: 1, name: 'Nova Conta', type: 'CHECKING', balance: 500 });

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/accounts');
    const newAccount: AccountResponse = { id: 'uuid-3', userId: 1, name: 'Nova Conta', type: 'CHECKING', balance: 500, createdAt: '', updatedAt: '' };
    req.flush(newAccount);

    expect(service.accounts().length).toBe(3);
    expect(service.accounts()[2].name).toBe('Nova Conta');
  });

  it('should update existing account', () => {
    service.accounts.set(mockAccounts);
    service.update('uuid-1', { userId: 1, name: 'Conta Actualizada', type: 'CHECKING', balance: 1500 });

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/accounts/uuid-1');
    req.flush({ ...mockAccounts[0], name: 'Conta Actualizada' });

    expect(service.accounts()[0].name).toBe('Conta Actualizada');
  });

  it('should remove account after delete', () => {
    service.accounts.set(mockAccounts);
    service.delete('uuid-1');

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/accounts/uuid-1');
    req.flush(null);

    expect(service.accounts().length).toBe(1);
    expect(service.accounts()[0].id).toBe('uuid-2');
  });

  it('should set error when load fails', () => {
    service.loadByUser(1);
    const req = httpTesting.expectOne('http://localhost:8080/api/v1/accounts/user/1');
    req.flush('Error', { status: 500, statusText: 'Server Error' });

    expect(service.error()).toBe('Erro ao carregar contas');
  });
});