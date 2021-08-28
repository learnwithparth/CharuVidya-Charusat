import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignCategoryReviewerComponent } from './assign-category-reviewer.component';

describe('AssignCategoryReviewerComponent', () => {
  let component: AssignCategoryReviewerComponent;
  let fixture: ComponentFixture<AssignCategoryReviewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssignCategoryReviewerComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignCategoryReviewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
