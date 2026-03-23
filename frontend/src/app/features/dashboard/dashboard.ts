import { Component, inject, OnInit, computed, signal } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { User } from '../../core/services/user';
import { UserResponse } from '../../core/models/user.model';

@Component({
  selector: 'app-dashboard',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatIconModule,
    RouterLink
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  protected userService = inject(User);

  protected kpis = signal({
    totalBalance: 12480,
    income: 3200,
    expenses: 2880,
    savings: 320
  });

  protected greeting = computed(() => {
    const hour = new Date().getHours();
    if (hour >= 6 && hour < 12) return 'Bom dia';
    if (hour >= 12 && hour < 18) return 'Boa tarde';
    return 'Boa noite';
  });

  protected showOverlay = computed(() => !this.userService.currentUser());

  ngOnInit(): void {
    this.userService.loadAll();
  }

  protected onUserChange(user: UserResponse): void {
    this.userService.setCurrentUser(user);
    this.loadKpis();
  }

  private loadKpis(): void {
    this.kpis.set({
      totalBalance: 12480,
      income: 3200,
      expenses: 2880,
      savings: 320
    });
  }
}