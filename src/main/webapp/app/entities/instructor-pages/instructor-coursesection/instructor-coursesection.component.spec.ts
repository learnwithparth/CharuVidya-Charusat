import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstructorCoursesectionComponent } from './instructor-coursesection.component';

describe('InstructorCoursesectionComponent', () => {
  let component: InstructorCoursesectionComponent;
  let fixture: ComponentFixture<InstructorCoursesectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InstructorCoursesectionComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InstructorCoursesectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
