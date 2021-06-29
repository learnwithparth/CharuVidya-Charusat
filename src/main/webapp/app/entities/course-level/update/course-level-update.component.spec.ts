jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseLevelService } from '../service/course-level.service';
import { ICourseLevel, CourseLevel } from '../course-level.model';

import { CourseLevelUpdateComponent } from './course-level-update.component';

describe('Component Tests', () => {
  describe('CourseLevel Management Update Component', () => {
    let comp: CourseLevelUpdateComponent;
    let fixture: ComponentFixture<CourseLevelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseLevelService: CourseLevelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseLevelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseLevelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseLevelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseLevelService = TestBed.inject(CourseLevelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const courseLevel: ICourseLevel = { id: 456 };

        activatedRoute.data = of({ courseLevel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseLevel));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseLevel = { id: 123 };
        spyOn(courseLevelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseLevel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseLevelService.update).toHaveBeenCalledWith(courseLevel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseLevel = new CourseLevel();
        spyOn(courseLevelService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseLevel }));
        saveSubject.complete();

        // THEN
        expect(courseLevelService.create).toHaveBeenCalledWith(courseLevel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseLevel = { id: 123 };
        spyOn(courseLevelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseLevelService.update).toHaveBeenCalledWith(courseLevel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
