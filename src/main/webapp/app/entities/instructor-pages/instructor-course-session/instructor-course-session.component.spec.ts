import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstructorCourseSessionComponent } from './instructor-course-session.component';

describe('InstructorCourseSessionComponent', () => {
  let component: InstructorCourseSessionComponent;
  let fixture: ComponentFixture<InstructorCourseSessionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InstructorCourseSessionComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InstructorCourseSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
