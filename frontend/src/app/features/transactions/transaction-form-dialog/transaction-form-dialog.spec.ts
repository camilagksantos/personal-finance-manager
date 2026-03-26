import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TransactionFormDialog } from './transaction-form-dialog';

describe('TransactionFormDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: vi.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { accounts: [], categories: [] } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TransactionFormDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});