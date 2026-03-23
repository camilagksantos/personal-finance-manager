import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TransactionResponse, TransactionType } from '../../../core/models/transaction.model';
import { AccountResponse } from '../../../core/models/account.model';
import { CategoryResponse } from '../../../core/models/category.model';
import { format } from 'date-fns';

export interface TransactionFormDialogData {
  transaction?: TransactionResponse;
  accounts: AccountResponse[];
  categories: CategoryResponse[];
}

@Component({
  selector: 'app-transaction-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './transaction-form-dialog.html',
  styleUrl: './transaction-form-dialog.scss'
})
export class TransactionFormDialog implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<TransactionFormDialog>);
  protected data: TransactionFormDialogData = inject(MAT_DIALOG_DATA);

  protected isEdit = signal(false);

  protected transactionTypes: { value: TransactionType; label: string }[] = [
    { value: 'INCOME', label: 'Receita' },
    { value: 'EXPENSE', label: 'Despesa' }
  ];

  protected form = this.fb.group({
    accountId: ['', [Validators.required]],
    categoryId: ['', [Validators.required]],
    amount: [0, [Validators.required, Validators.min(0.01)]],
    type: [null as TransactionType | null, [Validators.required]],
    description: [''],
    date: [new Date(), [Validators.required]]
  });

  ngOnInit(): void {
    if (this.data.transaction) {
      this.isEdit.set(true);
      this.form.patchValue({
        accountId: this.data.transaction.accountId,
        categoryId: this.data.transaction.categoryId,
        amount: this.data.transaction.amount,
        type: this.data.transaction.type,
        description: this.data.transaction.description ?? '',
        date: new Date(this.data.transaction.date)
      });
    }
  }

  protected submit(): void {
    if (this.form.invalid) return;

    this.dialogRef.close({
      accountId: this.form.value.accountId!,
      categoryId: this.form.value.categoryId!,
      amount: this.form.value.amount!,
      type: this.form.value.type!,
      description: this.form.value.description ?? undefined,
      date: format(this.form.value.date!, 'yyyy-MM-dd')
    });
  }

  protected cancel(): void {
    this.dialogRef.close();
  }
}