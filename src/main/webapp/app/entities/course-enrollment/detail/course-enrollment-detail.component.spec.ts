import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseEnrollmentDetailComponent } from './course-enrollment-detail.component';

describe('Component Tests', () => {
  describe('CourseEnrollment Management Detail Component', () => {
    let comp: CourseEnrollmentDetailComponent;
    let fixture: ComponentFixture<CourseEnrollmentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseEnrollmentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseEnrollment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseEnrollmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseEnrollmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseEnrollment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseEnrollment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
