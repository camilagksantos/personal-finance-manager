import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AccountFormDialog } from './account-form-dialog';

describe('AccountFormDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: vi.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { userId: 1 } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(AccountFormDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});