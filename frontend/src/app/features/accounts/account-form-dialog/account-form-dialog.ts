import { Component, inject, signal, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { AccountResponse, AccountType } from '../../../core/models/account.model';

export interface AccountFormDialogData {
  account?: AccountResponse;
  userId: number;
}

@Component({
  selector: 'app-account-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './account-form-dialog.html',
  styleUrl: './account-form-dialog.scss'
})
export class AccountFormDialog implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<AccountFormDialog>);
  protected data: AccountFormDialogData = inject(MAT_DIALOG_DATA);

  protected isEdit = signal(false);

  protected accountTypes: { value: AccountType; label: string }[] = [
    { value: 'CHECKING', label: 'Conta à ordem' },
    { value: 'SAVINGS', label: 'Conta poupança' },
    { value: 'CREDIT_CARD', label: 'Cartão de crédito' }
  ];

  protected form = this.fb.group({
    name: ['', [Validators.required]],
    type: [null as AccountType | null, [Validators.required]],
    balance: [0, [Validators.required, Validators.min(0)]]
  });

  ngOnInit(): void {
    if (this.data.account) {
      this.isEdit.set(true);
      this.form.patchValue({
        name: this.data.account.name,
        type: this.data.account.type,
        balance: this.data.account.balance
      });
      this.form.get('balance')?.disable();
    }
  }

  protected submit(): void {
    if (this.form.invalid) return;

    this.dialogRef.close({
      userId: this.data.userId,
      name: this.form.value.name!,
      type: this.form.value.type!,
      balance: this.form.value.balance!
    });
  }

  protected cancel(): void {
    this.dialogRef.close();
  }
}