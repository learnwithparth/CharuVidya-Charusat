jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseSessionService } from '../service/course-session.service';
import { ICourseSession, CourseSession } from '../course-session.model';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';

import { CourseSessionUpdateComponent } from './course-session-update.component';

describe('Component Tests', () => {
  describe('CourseSession Management Update Component', () => {
    let comp: CourseSessionUpdateComponent;
    let fixture: ComponentFixture<CourseSessionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseSessionService: CourseSessionService;
    let courseSectionService: CourseSectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseSessionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseSessionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseSessionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseSessionService = TestBed.inject(CourseSessionService);
      courseSectionService = TestBed.inject(CourseSectionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CourseSection query and add missing value', () => {
        const courseSession: ICourseSession = { id: 456 };
        const courseSection: ICourseSection = { id: 6682 };
        courseSession.courseSection = courseSection;

        const courseSectionCollection: ICourseSection[] = [{ id: 22403 }];
        spyOn(courseSectionService, 'query').and.returnValue(of(new HttpResponse({ body: courseSectionCollection })));
        const additionalCourseSections = [courseSection];
        const expectedCollection: ICourseSection[] = [...additionalCourseSections, ...courseSectionCollection];
        spyOn(courseSectionService, 'addCourseSectionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courseSession });
        comp.ngOnInit();

        expect(courseSectionService.query).toHaveBeenCalled();
        expect(courseSectionService.addCourseSectionToCollectionIfMissing).toHaveBeenCalledWith(
          courseSectionCollection,
          ...additionalCourseSections
        );
        expect(comp.courseSectionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courseSession: ICourseSession = { id: 456 };
        const courseSection: ICourseSection = { id: 88692 };
        courseSession.courseSection = courseSection;

        activatedRoute.data = of({ courseSession });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseSession));
        expect(comp.courseSectionsSharedCollection).toContain(courseSection);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseSession = { id: 123 };
        spyOn(courseSessionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseSession });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseSession }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseSessionService.update).toHaveBeenCalledWith(courseSession);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseSession = new CourseSession();
        spyOn(courseSessionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseSession });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseSession }));
        saveSubject.complete();

        // THEN
        expect(courseSessionService.create).toHaveBeenCalledWith(courseSession);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseSession = { id: 123 };
        spyOn(courseSessionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseSession });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseSessionService.update).toHaveBeenCalledWith(courseSession);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCourseSectionById', () => {
        it('Should return tracked CourseSection primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseSectionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
