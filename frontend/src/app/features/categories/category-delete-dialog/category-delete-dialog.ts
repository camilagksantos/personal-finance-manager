import { Component, inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CategoryResponse } from '../../../core/models/category.model';

@Component({
  selector: 'app-category-delete-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './category-delete-dialog.html',
  styleUrl: './category-delete-dialog.scss'
})
export class CategoryDeleteDialog {
  private dialogRef = inject(MatDialogRef<CategoryDeleteDialog>);
  protected data: CategoryResponse = inject(MAT_DIALOG_DATA);

  protected confirm(): void {
    this.dialogRef.close(true);
  }

  protected cancel(): void {
    this.dialogRef.close(false);
  }
}