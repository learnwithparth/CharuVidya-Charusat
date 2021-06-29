jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseSectionService } from '../service/course-section.service';
import { ICourseSection, CourseSection } from '../course-section.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { CourseSectionUpdateComponent } from './course-section-update.component';

describe('Component Tests', () => {
  describe('CourseSection Management Update Component', () => {
    let comp: CourseSectionUpdateComponent;
    let fixture: ComponentFixture<CourseSectionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseSectionService: CourseSectionService;
    let courseService: CourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseSectionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseSectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseSectionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseSectionService = TestBed.inject(CourseSectionService);
      courseService = TestBed.inject(CourseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Course query and add missing value', () => {
        const courseSection: ICourseSection = { id: 456 };
        const course: ICourse = { id: 81851 };
        courseSection.course = course;

        const courseCollection: ICourse[] = [{ id: 32123 }];
        spyOn(courseService, 'query').and.returnValue(of(new HttpResponse({ body: courseCollection })));
        const additionalCourses = [course];
        const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
        spyOn(courseService, 'addCourseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courseSection });
        comp.ngOnInit();

        expect(courseService.query).toHaveBeenCalled();
        expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
        expect(comp.coursesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courseSection: ICourseSection = { id: 456 };
        const course: ICourse = { id: 72116 };
        courseSection.course = course;

        activatedRoute.data = of({ courseSection });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseSection));
        expect(comp.coursesSharedCollection).toContain(course);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseSection = { id: 123 };
        spyOn(courseSectionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseSection });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseSection }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseSectionService.update).toHaveBeenCalledWith(courseSection);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseSection = new CourseSection();
        spyOn(courseSectionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseSection });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseSection }));
        saveSubject.complete();

        // THEN
        expect(courseSectionService.create).toHaveBeenCalledWith(courseSection);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseSection = { id: 123 };
        spyOn(courseSectionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseSection });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseSectionService.update).toHaveBeenCalledWith(courseSection);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCourseById', () => {
        it('Should return tracked Course primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
