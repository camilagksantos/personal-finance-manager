import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';

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
                loadComponent: () => import('./features/accounts/accounts').then(c => c.Accounts)
            },
            {
                path: 'transactions',
                loadComponent: () => import('./features/transactions/transactions').then(c => c.Transactions)
            },
            {
                path: 'categories',
                loadComponent: () => import('./features/categories/categories').then(c => c.Categories)
            },
            {
                path: 'reports',
                loadComponent: () => import('./features/reports/reports').then(c => c.Reports)
            }
        ]
    },
    {
        path: '**',
        redirectTo: 'dashboard'
    }
];
