import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { DatePipe } from '@angular/common';
import { Category } from '../../core/services/category';
import { User } from '../../core/services/user';
import { CategoryResponse, CategoryType } from '../../core/models/category.model';
import { CategoryFormDialog, CategoryFormDialogData } from './category-form-dialog/category-form-dialog';
import { CategoryDeleteDialog } from './category-delete-dialog/category-delete-dialog';

@Component({
  selector: 'app-categories',
  imports: [
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTooltipModule,
    MatProgressBarModule,
    FormsModule,
    DatePipe,
    NgClass
  ],
  templateUrl: './categories.html',
  styleUrl: './categories.scss'
})
export class Categories implements OnInit {
  protected categoryService = inject(Category);
  private userService = inject(User);
  private dialog = inject(MatDialog);

  protected loading = this.categoryService.loading;
  protected error = this.categoryService.error;

  protected searchTerm = signal('');
  protected selectedType = signal<CategoryType | ''>('');

  protected readonly categoryTypeLabels: Record<CategoryType, string> = {
    INCOME: 'Receita',
    EXPENSE: 'Despesa'
  };

  protected readonly categoryTypeColors: Record<CategoryType, string> = {
    INCOME: 'badge--green',
    EXPENSE: 'badge--red'
  };

  protected readonly categoryTypes: { value: CategoryType | ''; label: string }[] = [
    { value: '', label: 'Todos os tipos' },
    { value: 'INCOME', label: 'Receita' },
    { value: 'EXPENSE', label: 'Despesa' }
  ];

  protected displayedColumns = ['name', 'type', 'createdAt', 'actions'];

  protected filteredCategories = computed(() => {
    let result = this.categoryService.categories();

    const term = this.searchTerm().trim().toLowerCase();
    if (term) {
      result = result.filter(c => c.name.toLowerCase().includes(term));
    }

    if (this.selectedType()) {
      result = result.filter(c => c.type === this.selectedType());
    }

    return result.sort((a, b) => a.name.localeCompare(b.name));
  });

  ngOnInit(): void {
    const user = this.userService.currentUser();
    if (!user) return;
    this.categoryService.loadByUser(user.id);
  }

  protected getTypeColor(type: string): string {
    return this.categoryTypeColors[type as CategoryType] ?? '';
  }

  protected getTypeLabel(type: string): string {
    return this.categoryTypeLabels[type as CategoryType] ?? type;
  }

  protected openCreateDialog(): void {
    const user = this.userService.currentUser();
    if (!user) return;

    const dialogRef = this.dialog.open(CategoryFormDialog, {
      data: { userId: user.id } satisfies CategoryFormDialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.categoryService.create(result);
    });
  }

  protected openEditDialog(category: CategoryResponse): void {
    const user = this.userService.currentUser();
    if (!user) return;

    const dialogRef = this.dialog.open(CategoryFormDialog, {
      data: { category, userId: user.id } satisfies CategoryFormDialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.categoryService.update(category.id, result);
    });
  }

  protected openDeleteDialog(category: CategoryResponse): void {
    const dialogRef = this.dialog.open(CategoryDeleteDialog, {
      data: category
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) this.categoryService.delete(category.id);
    });
  }
}