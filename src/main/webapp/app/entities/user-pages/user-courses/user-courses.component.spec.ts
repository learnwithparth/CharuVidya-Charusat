import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCoursesComponent } from './user-courses.component';

describe('UserCoursesComponent', () => {
  let component: UserCoursesComponent;
  let fixture: ComponentFixture<UserCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCoursesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
