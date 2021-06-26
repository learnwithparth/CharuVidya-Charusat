jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseProgressService } from '../service/course-progress.service';
import { ICourseProgress, CourseProgress } from '../course-progress.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { CourseSessionService } from 'app/entities/course-session/service/course-session.service';

import { CourseProgressUpdateComponent } from './course-progress-update.component';

describe('Component Tests', () => {
  describe('CourseProgress Management Update Component', () => {
    let comp: CourseProgressUpdateComponent;
    let fixture: ComponentFixture<CourseProgressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseProgressService: CourseProgressService;
    let userService: UserService;
    let courseSessionService: CourseSessionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseProgressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseProgressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseProgressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseProgressService = TestBed.inject(CourseProgressService);
      userService = TestBed.inject(UserService);
      courseSessionService = TestBed.inject(CourseSessionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const courseProgress: ICourseProgress = { id: 456 };
        const user: IUser = { id: 30191 };
        courseProgress.user = user;

        const userCollection: IUser[] = [{ id: 13251 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courseProgress });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CourseSession query and add missing value', () => {
        const courseProgress: ICourseProgress = { id: 456 };
        const courseSession: ICourseSession = { id: 42853 };
        courseProgress.courseSession = courseSession;

        const courseSessionCollection: ICourseSession[] = [{ id: 14282 }];
        spyOn(courseSessionService, 'query').and.returnValue(of(new HttpResponse({ body: courseSessionCollection })));
        const additionalCourseSessions = [courseSession];
        const expectedCollection: ICourseSession[] = [...additionalCourseSessions, ...courseSessionCollection];
        spyOn(courseSessionService, 'addCourseSessionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courseProgress });
        comp.ngOnInit();

        expect(courseSessionService.query).toHaveBeenCalled();
        expect(courseSessionService.addCourseSessionToCollectionIfMissing).toHaveBeenCalledWith(
          courseSessionCollection,
          ...additionalCourseSessions
        );
        expect(comp.courseSessionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courseProgress: ICourseProgress = { id: 456 };
        const user: IUser = { id: 16957 };
        courseProgress.user = user;
        const courseSession: ICourseSession = { id: 99505 };
        courseProgress.courseSession = courseSession;

        activatedRoute.data = of({ courseProgress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseProgress));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.courseSessionsSharedCollection).toContain(courseSession);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseProgress = { id: 123 };
        spyOn(courseProgressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseProgress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseProgress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseProgressService.update).toHaveBeenCalledWith(courseProgress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseProgress = new CourseProgress();
        spyOn(courseProgressService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseProgress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseProgress }));
        saveSubject.complete();

        // THEN
        expect(courseProgressService.create).toHaveBeenCalledWith(courseProgress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseProgress = { id: 123 };
        spyOn(courseProgressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseProgress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseProgressService.update).toHaveBeenCalledWith(courseProgress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCourseSessionById', () => {
        it('Should return tracked CourseSession primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseSessionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
