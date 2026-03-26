import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CategoryDeleteDialog } from './category-delete-dialog';

describe('CategoryDeleteDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryDeleteDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: vi.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { id: '1', name: 'Test', type: 'EXPENSE', userId: 1, createdAt: '', updatedAt: '' } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(CategoryDeleteDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});