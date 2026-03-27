import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { User } from './user';
import { UserResponse } from '../models/user.model';

const mockUsers: UserResponse[] = [
  { id: 1, name: 'Alice', username: 'alice', email: 'alice@test.com', phone: '123' },
  { id: 2, name: 'Bruno', username: 'bruno', email: 'bruno@test.com', phone: '456' }
];

describe('User', () => {
  let service: User;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(User);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
    localStorage.clear();
  });

  it('should initialise currentUser as null when localStorage is empty', () => {
    expect(service.currentUser()).toBeNull();
  });

  it('should restore currentUser from localStorage on init', () => {
    localStorage.setItem('finance_current_user', JSON.stringify(mockUsers[0]));

    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });

    const freshService = TestBed.inject(User);
    expect(freshService.currentUser()?.id).toBe(1);
  });

  it('should set currentUser and persist to localStorage', () => {
    service.setCurrentUser(mockUsers[0]);
    expect(service.currentUser()?.name).toBe('Alice');
    const stored = JSON.parse(localStorage.getItem('finance_current_user')!);
    expect(stored.id).toBe(1);
  });

  it('should clear currentUser and remove from localStorage', () => {
    service.setCurrentUser(mockUsers[0]);
    service.clearCurrentUser();
    expect(service.currentUser()).toBeNull();
    expect(localStorage.getItem('finance_current_user')).toBeNull();
  });

  it('should load users and set loading state correctly', () => {
    service.loadAll();
    expect(service.loading()).toBe(true);

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/users');
    req.flush(mockUsers);

    expect(service.loading()).toBe(false);
    expect(service.users().length).toBe(2);
    expect(service.users()[0].name).toBe('Alice');
  });

  it('should set error when loadAll fails', () => {
    service.loadAll();
    const req = httpTesting.expectOne('http://localhost:8080/api/v1/users');
    req.flush('Error', { status: 500, statusText: 'Server Error' });

    expect(service.error()).toBe('Erro ao carregar utilizadores');
    expect(service.loading()).toBe(false);
  });
});