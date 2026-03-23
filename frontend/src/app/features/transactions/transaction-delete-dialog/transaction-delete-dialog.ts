import { Component, inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { TransactionResponse } from '../../../core/models/transaction.model';

@Component({
  selector: 'app-transaction-delete-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './transaction-delete-dialog.html',
  styleUrl: './transaction-delete-dialog.scss'
})
export class TransactionDeleteDialog {
  private dialogRef = inject(MatDialogRef<TransactionDeleteDialog>);
  protected data: TransactionResponse = inject(MAT_DIALOG_DATA);

  protected confirm(): void {
    this.dialogRef.close(true);
  }

  protected cancel(): void {
    this.dialogRef.close(false);
  }
}