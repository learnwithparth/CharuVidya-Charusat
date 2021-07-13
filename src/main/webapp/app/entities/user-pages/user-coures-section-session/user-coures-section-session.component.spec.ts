import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCouresSectionSessionComponent } from './user-coures-section-session.component';

describe('UserCouresSectionSessionComponent', () => {
  let component: UserCouresSectionSessionComponent;
  let fixture: ComponentFixture<UserCouresSectionSessionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCouresSectionSessionComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCouresSectionSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
