import { TestBed } from '@angular/core/testing';
import { provideRouter, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { authGuard } from './auth-guard';
import { User } from '../services/user';

describe('authGuard', () => {
  let router: Router;
  let userService: User;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient()
      ]
    });
    router = TestBed.inject(Router);
    userService = TestBed.inject(User);
  });

  afterEach(() => localStorage.clear());

  const runGuard = () => TestBed.runInInjectionContext(() =>
    authGuard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot)
  );

  it('should return true when user is selected', () => {
    userService.setCurrentUser({ id: 1, name: 'Alice', username: 'alice', email: 'alice@test.com', phone: '123' });
    const result = runGuard();
    expect(result).toBe(true);
  });

  it('should redirect to /dashboard when no user is selected', () => {
    const result = runGuard();
    expect(result).not.toBe(true);
    expect(result.toString()).toContain('dashboard');
  });
});