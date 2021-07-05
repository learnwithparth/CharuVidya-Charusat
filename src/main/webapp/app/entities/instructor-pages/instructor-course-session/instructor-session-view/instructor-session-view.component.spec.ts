import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstructorSessionViewComponent } from './instructor-session-view.component';

describe('InstructorSessionViewComponent', () => {
  let component: InstructorSessionViewComponent;
  let fixture: ComponentFixture<InstructorSessionViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstructorSessionViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InstructorSessionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
