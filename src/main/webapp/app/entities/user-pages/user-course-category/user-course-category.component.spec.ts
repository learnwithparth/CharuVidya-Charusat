import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseCategoryComponent } from './user-course-category.component';

describe('UserCourseCategoryComponent', () => {
  let component: UserCourseCategoryComponent;
  let fixture: ComponentFixture<UserCourseCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseCategoryComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
