import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseProgressDetailComponent } from './course-progress-detail.component';

describe('Component Tests', () => {
  describe('CourseProgress Management Detail Component', () => {
    let comp: CourseProgressDetailComponent;
    let fixture: ComponentFixture<CourseProgressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseProgressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseProgress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseProgressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseProgressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseProgress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseProgress).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
