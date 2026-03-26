import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TransactionDeleteDialog } from './transaction-delete-dialog';

describe('TransactionDeleteDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionDeleteDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: vi.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { id: '1', accountId: '1', categoryId: '1', amount: 100, type: 'EXPENSE', date: '2026-01-01', createdAt: '', updatedAt: '' } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TransactionDeleteDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});