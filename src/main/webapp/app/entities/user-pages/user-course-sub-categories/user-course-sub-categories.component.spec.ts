import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseSubCategoriesComponent } from './user-course-sub-categories.component';

describe('UserCourseSubCategoriesComponent', () => {
  let component: UserCourseSubCategoriesComponent;
  let fixture: ComponentFixture<UserCourseSubCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseSubCategoriesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseSubCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
