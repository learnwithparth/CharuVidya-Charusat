import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseSectionDetailComponent } from './course-section-detail.component';

describe('Component Tests', () => {
  describe('CourseSection Management Detail Component', () => {
    let comp: CourseSectionDetailComponent;
    let fixture: ComponentFixture<CourseSectionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseSectionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseSection: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseSectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseSectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseSection on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseSection).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
