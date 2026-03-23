import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryDeleteDialog } from './category-delete-dialog';

describe('CategoryDeleteDialog', () => {
  let component: CategoryDeleteDialog;
  let fixture: ComponentFixture<CategoryDeleteDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryDeleteDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoryDeleteDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
