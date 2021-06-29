import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICourse, Course } from '../course.model';

import { CourseService } from './course.service';

describe('Service Tests', () => {
  describe('Course Service', () => {
    let service: CourseService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourse;
    let expectedResult: ICourse | ICourse[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        courseTitle: 'AAAAAAA',
        courseDescription: 'AAAAAAA',
        courseObjectives: 'AAAAAAA',
        courseSubTitle: 'AAAAAAA',
        previewVideourl: 'AAAAAAA',
        courseLength: 0,
        logo: 'AAAAAAA',
        courseCreatedOn: currentDate,
        courseUpdatedOn: currentDate,
        courseRootDir: 'AAAAAAA',
        amount: 0,
        isDraft: false,
        isApproved: false,
        courseApprovalDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            courseCreatedOn: currentDate.format(DATE_FORMAT),
            courseUpdatedOn: currentDate.format(DATE_FORMAT),
            courseApprovalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Course', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            courseCreatedOn: currentDate.format(DATE_FORMAT),
            courseUpdatedOn: currentDate.format(DATE_FORMAT),
            courseApprovalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            courseCreatedOn: currentDate,
            courseUpdatedOn: currentDate,
            courseApprovalDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Course()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Course', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            courseTitle: 'BBBBBB',
            courseDescription: 'BBBBBB',
            courseObjectives: 'BBBBBB',
            courseSubTitle: 'BBBBBB',
            previewVideourl: 'BBBBBB',
            courseLength: 1,
            logo: 'BBBBBB',
            courseCreatedOn: currentDate.format(DATE_FORMAT),
            courseUpdatedOn: currentDate.format(DATE_FORMAT),
            courseRootDir: 'BBBBBB',
            amount: 1,
            isDraft: true,
            isApproved: true,
            courseApprovalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            courseCreatedOn: currentDate,
            courseUpdatedOn: currentDate,
            courseApprovalDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Course', () => {
        const patchObject = Object.assign(
          {
            courseTitle: 'BBBBBB',
            courseDescription: 'BBBBBB',
            courseObjectives: 'BBBBBB',
            courseSubTitle: 'BBBBBB',
            previewVideourl: 'BBBBBB',
            courseLength: 1,
            amount: 1,
            isApproved: true,
          },
          new Course()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            courseCreatedOn: currentDate,
            courseUpdatedOn: currentDate,
            courseApprovalDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Course', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            courseTitle: 'BBBBBB',
            courseDescription: 'BBBBBB',
            courseObjectives: 'BBBBBB',
            courseSubTitle: 'BBBBBB',
            previewVideourl: 'BBBBBB',
            courseLength: 1,
            logo: 'BBBBBB',
            courseCreatedOn: currentDate.format(DATE_FORMAT),
            courseUpdatedOn: currentDate.format(DATE_FORMAT),
            courseRootDir: 'BBBBBB',
            amount: 1,
            isDraft: true,
            isApproved: true,
            courseApprovalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            courseCreatedOn: currentDate,
            courseUpdatedOn: currentDate,
            courseApprovalDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Course', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseToCollectionIfMissing', () => {
        it('should add a Course to an empty array', () => {
          const course: ICourse = { id: 123 };
          expectedResult = service.addCourseToCollectionIfMissing([], course);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(course);
        });

        it('should not add a Course to an array that contains it', () => {
          const course: ICourse = { id: 123 };
          const courseCollection: ICourse[] = [
            {
              ...course,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseToCollectionIfMissing(courseCollection, course);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Course to an array that doesn't contain it", () => {
          const course: ICourse = { id: 123 };
          const courseCollection: ICourse[] = [{ id: 456 }];
          expectedResult = service.addCourseToCollectionIfMissing(courseCollection, course);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(course);
        });

        it('should add only unique Course to an array', () => {
          const courseArray: ICourse[] = [{ id: 123 }, { id: 456 }, { id: 98425 }];
          const courseCollection: ICourse[] = [{ id: 123 }];
          expectedResult = service.addCourseToCollectionIfMissing(courseCollection, ...courseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const course: ICourse = { id: 123 };
          const course2: ICourse = { id: 456 };
          expectedResult = service.addCourseToCollectionIfMissing([], course, course2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(course);
          expect(expectedResult).toContain(course2);
        });

        it('should accept null and undefined values', () => {
          const course: ICourse = { id: 123 };
          expectedResult = service.addCourseToCollectionIfMissing([], null, course, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(course);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
