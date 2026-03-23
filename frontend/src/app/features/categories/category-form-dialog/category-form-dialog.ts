import { Component, inject, signal, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { CategoryResponse, CategoryType } from '../../../core/models/category.model';

export interface CategoryFormDialogData {
  category?: CategoryResponse;
  userId: number;
}

@Component({
  selector: 'app-category-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './category-form-dialog.html',
  styleUrl: './category-form-dialog.scss'
})
export class CategoryFormDialog implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<CategoryFormDialog>);
  protected data: CategoryFormDialogData = inject(MAT_DIALOG_DATA);

  protected isEdit = signal(false);

  protected categoryTypes: { value: CategoryType; label: string }[] = [
    { value: 'INCOME', label: 'Receita' },
    { value: 'EXPENSE', label: 'Despesa' }
  ];

  protected form = this.fb.group({
    name: ['', [Validators.required]],
    type: [null as CategoryType | null, [Validators.required]]
  });

  ngOnInit(): void {
    if (this.data.category) {
      this.isEdit.set(true);
      this.form.patchValue({
        name: this.data.category.name,
        type: this.data.category.type
      });
    }
  }

  protected submit(): void {
    if (this.form.invalid) return;

    this.dialogRef.close({
      userId: this.data.userId,
      name: this.form.value.name!,
      type: this.form.value.type!
    });
  }

  protected cancel(): void {
    this.dialogRef.close();
  }
}