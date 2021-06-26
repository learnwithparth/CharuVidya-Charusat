jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseCategoryService } from '../service/course-category.service';
import { ICourseCategory, CourseCategory } from '../course-category.model';

import { CourseCategoryUpdateComponent } from './course-category-update.component';

describe('Component Tests', () => {
  describe('CourseCategory Management Update Component', () => {
    let comp: CourseCategoryUpdateComponent;
    let fixture: ComponentFixture<CourseCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseCategoryService: CourseCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseCategoryService = TestBed.inject(CourseCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const courseCategory: ICourseCategory = { id: 456 };

        activatedRoute.data = of({ courseCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseCategory = { id: 123 };
        spyOn(courseCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseCategoryService.update).toHaveBeenCalledWith(courseCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseCategory = new CourseCategory();
        spyOn(courseCategoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseCategory }));
        saveSubject.complete();

        // THEN
        expect(courseCategoryService.create).toHaveBeenCalledWith(courseCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseCategory = { id: 123 };
        spyOn(courseCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseCategoryService.update).toHaveBeenCalledWith(courseCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
