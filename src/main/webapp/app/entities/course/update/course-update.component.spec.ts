jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseService } from '../service/course.service';
import { ICourse, Course } from '../course.model';
import { ICourseLevel } from 'app/entities/course-level/course-level.model';
import { CourseLevelService } from 'app/entities/course-level/service/course-level.service';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { CourseCategoryService } from 'app/entities/course-category/service/course-category.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Component Tests', () => {
  describe('Course Management Update Component', () => {
    let comp: CourseUpdateComponent;
    let fixture: ComponentFixture<CourseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseService: CourseService;
    let courseLevelService: CourseLevelService;
    let courseCategoryService: CourseCategoryService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseService = TestBed.inject(CourseService);
      courseLevelService = TestBed.inject(CourseLevelService);
      courseCategoryService = TestBed.inject(CourseCategoryService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CourseLevel query and add missing value', () => {
        const course: ICourse = { id: 456 };
        const courseLevel: ICourseLevel = { id: 29406 };
        course.courseLevel = courseLevel;

        const courseLevelCollection: ICourseLevel[] = [{ id: 10989 }];
        spyOn(courseLevelService, 'query').and.returnValue(of(new HttpResponse({ body: courseLevelCollection })));
        const additionalCourseLevels = [courseLevel];
        const expectedCollection: ICourseLevel[] = [...additionalCourseLevels, ...courseLevelCollection];
        spyOn(courseLevelService, 'addCourseLevelToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ course });
        comp.ngOnInit();

        expect(courseLevelService.query).toHaveBeenCalled();
        expect(courseLevelService.addCourseLevelToCollectionIfMissing).toHaveBeenCalledWith(
          courseLevelCollection,
          ...additionalCourseLevels
        );
        expect(comp.courseLevelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CourseCategory query and add missing value', () => {
        const course: ICourse = { id: 456 };
        const courseCategory: ICourseCategory = { id: 20270 };
        course.courseCategory = courseCategory;

        const courseCategoryCollection: ICourseCategory[] = [{ id: 10154 }];
        spyOn(courseCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: courseCategoryCollection })));
        const additionalCourseCategories = [courseCategory];
        const expectedCollection: ICourseCategory[] = [...additionalCourseCategories, ...courseCategoryCollection];
        spyOn(courseCategoryService, 'addCourseCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ course });
        comp.ngOnInit();

        expect(courseCategoryService.query).toHaveBeenCalled();
        expect(courseCategoryService.addCourseCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          courseCategoryCollection,
          ...additionalCourseCategories
        );
        expect(comp.courseCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const course: ICourse = { id: 456 };
        const user: IUser = { id: 40689 };
        course.user = user;
        const reviewer: IUser = { id: 13045 };
        course.reviewer = reviewer;

        const userCollection: IUser[] = [{ id: 11803 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, reviewer];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ course });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const course: ICourse = { id: 456 };
        const courseLevel: ICourseLevel = { id: 79009 };
        course.courseLevel = courseLevel;
        const courseCategory: ICourseCategory = { id: 23357 };
        course.courseCategory = courseCategory;
        const user: IUser = { id: 24607 };
        course.user = user;
        const reviewer: IUser = { id: 72720 };
        course.reviewer = reviewer;

        activatedRoute.data = of({ course });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(course));
        expect(comp.courseLevelsSharedCollection).toContain(courseLevel);
        expect(comp.courseCategoriesSharedCollection).toContain(courseCategory);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(reviewer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const course = { id: 123 };
        spyOn(courseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ course });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: course }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseService.update).toHaveBeenCalledWith(course);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const course = new Course();
        spyOn(courseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ course });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: course }));
        saveSubject.complete();

        // THEN
        expect(courseService.create).toHaveBeenCalledWith(course);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const course = { id: 123 };
        spyOn(courseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ course });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseService.update).toHaveBeenCalledWith(course);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCourseLevelById', () => {
        it('Should return tracked CourseLevel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseLevelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCourseCategoryById', () => {
        it('Should return tracked CourseCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
