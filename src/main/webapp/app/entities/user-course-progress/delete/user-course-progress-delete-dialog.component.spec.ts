import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseProgressDeleteDialogComponent } from './user-course-progress-delete-dialog.component';

describe('UserCourseProgressDeleteDialogComponent', () => {
  let component: UserCourseProgressDeleteDialogComponent;
  let fixture: ComponentFixture<UserCourseProgressDeleteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseProgressDeleteDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseProgressDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
