import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseProgressDetailComponent } from './user-course-progress-detail.component';

describe('UserCourseProgressDetailComponent', () => {
  let component: UserCourseProgressDetailComponent;
  let fixture: ComponentFixture<UserCourseProgressDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseProgressDetailComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseProgressDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
