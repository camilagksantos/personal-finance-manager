import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { User } from '../services/user';

export const authGuard: CanActivateFn = () => {
  const userService = inject(User);
  const router = inject(Router);

  if (userService.currentUser()) {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};