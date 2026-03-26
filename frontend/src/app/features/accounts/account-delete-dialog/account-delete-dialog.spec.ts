import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AccountDeleteDialog } from './account-delete-dialog';

describe('AccountDeleteDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountDeleteDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: vi.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { id: '1', name: 'Test', type: 'CHECKING', balance: 0, userId: 1, createdAt: '', updatedAt: '' } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(AccountDeleteDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});