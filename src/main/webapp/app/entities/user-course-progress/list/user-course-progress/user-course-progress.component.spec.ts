import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseProgressComponent } from './user-course-progress.component';

describe('UserCourseProgressComponent', () => {
  let component: UserCourseProgressComponent;
  let fixture: ComponentFixture<UserCourseProgressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseProgressComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
