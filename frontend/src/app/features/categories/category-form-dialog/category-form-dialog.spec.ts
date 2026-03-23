import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryFormDialog } from './category-form-dialog';

describe('CategoryFormDialog', () => {
  let component: CategoryFormDialog;
  let fixture: ComponentFixture<CategoryFormDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryFormDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoryFormDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
