import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseLevelDetailComponent } from './course-level-detail.component';

describe('Component Tests', () => {
  describe('CourseLevel Management Detail Component', () => {
    let comp: CourseLevelDetailComponent;
    let fixture: ComponentFixture<CourseLevelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseLevelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseLevel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseLevelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseLevelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseLevel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseLevel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
