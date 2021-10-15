import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseProgressUpdateComponent } from './user-course-progress-update.component';

describe('UserCourseProgressUpdateComponent', () => {
  let component: UserCourseProgressUpdateComponent;
  let fixture: ComponentFixture<UserCourseProgressUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseProgressUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseProgressUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
