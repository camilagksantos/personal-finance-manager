import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CategoryFormDialog } from './category-form-dialog';

describe('CategoryFormDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: vi.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { userId: 1 } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(CategoryFormDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});