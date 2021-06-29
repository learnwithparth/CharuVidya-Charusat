jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseEnrollmentService } from '../service/course-enrollment.service';
import { ICourseEnrollment, CourseEnrollment } from '../course-enrollment.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { CourseEnrollmentUpdateComponent } from './course-enrollment-update.component';

describe('Component Tests', () => {
  describe('CourseEnrollment Management Update Component', () => {
    let comp: CourseEnrollmentUpdateComponent;
    let fixture: ComponentFixture<CourseEnrollmentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseEnrollmentService: CourseEnrollmentService;
    let userService: UserService;
    let courseService: CourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseEnrollmentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseEnrollmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseEnrollmentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseEnrollmentService = TestBed.inject(CourseEnrollmentService);
      userService = TestBed.inject(UserService);
      courseService = TestBed.inject(CourseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const courseEnrollment: ICourseEnrollment = { id: 456 };
        const user: IUser = { id: 45289 };
        courseEnrollment.user = user;

        const userCollection: IUser[] = [{ id: 66497 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courseEnrollment });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Course query and add missing value', () => {
        const courseEnrollment: ICourseEnrollment = { id: 456 };
        const course: ICourse = { id: 29241 };
        courseEnrollment.course = course;

        const courseCollection: ICourse[] = [{ id: 57239 }];
        spyOn(courseService, 'query').and.returnValue(of(new HttpResponse({ body: courseCollection })));
        const additionalCourses = [course];
        const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
        spyOn(courseService, 'addCourseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courseEnrollment });
        comp.ngOnInit();

        expect(courseService.query).toHaveBeenCalled();
        expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
        expect(comp.coursesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courseEnrollment: ICourseEnrollment = { id: 456 };
        const user: IUser = { id: 92840 };
        courseEnrollment.user = user;
        const course: ICourse = { id: 89014 };
        courseEnrollment.course = course;

        activatedRoute.data = of({ courseEnrollment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courseEnrollment));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.coursesSharedCollection).toContain(course);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseEnrollment = { id: 123 };
        spyOn(courseEnrollmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseEnrollment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseEnrollment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseEnrollmentService.update).toHaveBeenCalledWith(courseEnrollment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseEnrollment = new CourseEnrollment();
        spyOn(courseEnrollmentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseEnrollment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courseEnrollment }));
        saveSubject.complete();

        // THEN
        expect(courseEnrollmentService.create).toHaveBeenCalledWith(courseEnrollment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courseEnrollment = { id: 123 };
        spyOn(courseEnrollmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courseEnrollment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseEnrollmentService.update).toHaveBeenCalledWith(courseEnrollment);
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
