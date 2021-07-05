import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCourseSectionsComponent } from './user-course-sections.component';

describe('UserCourseSectionsComponent', () => {
  let component: UserCourseSectionsComponent;
  let fixture: ComponentFixture<UserCourseSectionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCourseSectionsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCourseSectionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
