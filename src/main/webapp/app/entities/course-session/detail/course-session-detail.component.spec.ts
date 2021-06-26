import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseSessionDetailComponent } from './course-session-detail.component';

describe('Component Tests', () => {
  describe('CourseSession Management Detail Component', () => {
    let comp: CourseSessionDetailComponent;
    let fixture: ComponentFixture<CourseSessionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseSessionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseSession: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseSessionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseSessionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseSession on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseSession).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
