import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseSessionComponent } from './user-course-session.component';

describe('UserCourseSessionComponent', () => {
  let component: UserCourseSessionComponent;
  let fixture: ComponentFixture<UserCourseSessionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseSessionComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
