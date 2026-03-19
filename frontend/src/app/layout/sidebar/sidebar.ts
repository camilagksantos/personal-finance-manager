import { Component, inject } from '@angular/core';
import { Layout } from '../layout';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, MatIcon],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {
  protected layout = inject(Layout);

  protected navItems = [
    { path: '/dashboard', icon: 'dashboard', label: 'Dashboard' },
    { path: '/accounts', icon: 'account_balance', label: 'Accounts' },
    { path: '/transactions', icon: 'swap_horiz', label: 'Transactions' },
    { path: '/categories', icon: 'category', label: 'Categories' },
    { path: '/reports', icon: 'bar_chart', label: 'Reports' }
  ];
}
