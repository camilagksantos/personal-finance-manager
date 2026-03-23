import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionDeleteDialog } from './transaction-delete-dialog';

describe('TransactionDeleteDialog', () => {
  let component: TransactionDeleteDialog;
  let fixture: ComponentFixture<TransactionDeleteDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionDeleteDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(TransactionDeleteDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
