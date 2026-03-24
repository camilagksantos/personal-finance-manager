import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        children: [
            {
                path: '',
                redirectTo: 'dashboard',
                pathMatch: 'full'
            },
            {
                path: 'dashboard',
                loadComponent: () => import('./features/dashboard/dashboard').then(c => c.Dashboard)
            },
            {
                path: 'accounts',
                canActivate: [authGuard],
                loadComponent: () => import('./features/accounts/accounts').then(c => c.Accounts)
            },
            {
                path: 'transactions',
                canActivate: [authGuard],
                loadComponent: () => import('./features/transactions/transactions').then(c => c.Transactions)
            },
            {
                path: 'categories',
                canActivate: [authGuard],
                loadComponent: () => import('./features/categories/categories').then(c => c.Categories)
            },
            {
                path: 'reports',
                canActivate: [authGuard],
                loadComponent: () => import('./features/reports/reports').then(c => c.Reports)
            }
        ]
    },
    {
        path: '**',
        redirectTo: 'dashboard'
    }
];
