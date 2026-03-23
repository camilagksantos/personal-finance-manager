import { Component, inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { AccountResponse } from '../../../core/models/account.model';

@Component({
  selector: 'app-account-delete-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './account-delete-dialog.html',
  styleUrl: './account-delete-dialog.scss'
})
export class AccountDeleteDialog {
  private dialogRef = inject(MatDialogRef<AccountDeleteDialog>);
  protected data: AccountResponse = inject(MAT_DIALOG_DATA);

  protected confirm(): void {
    this.dialogRef.close(true);
  }

  protected cancel(): void {
    this.dialogRef.close(false);
  }
}