import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseCategoryDetailComponent } from './course-category-detail.component';

describe('Component Tests', () => {
  describe('CourseCategory Management Detail Component', () => {
    let comp: CourseCategoryDetailComponent;
    let fixture: ComponentFixture<CourseCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourseCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courseCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourseCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
