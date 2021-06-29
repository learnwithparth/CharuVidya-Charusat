import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICourseEnrollment, CourseEnrollment } from '../course-enrollment.model';

import { CourseEnrollmentService } from './course-enrollment.service';

describe('Service Tests', () => {
  describe('CourseEnrollment Service', () => {
    let service: CourseEnrollmentService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseEnrollment;
    let expectedResult: ICourseEnrollment | ICourseEnrollment[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseEnrollmentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        enrollementDate: currentDate,
        lastAccessedDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            enrollementDate: currentDate.format(DATE_FORMAT),
            lastAccessedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CourseEnrollment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            enrollementDate: currentDate.format(DATE_FORMAT),
            lastAccessedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            enrollementDate: currentDate,
            lastAccessedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new CourseEnrollment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourseEnrollment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            enrollementDate: currentDate.format(DATE_FORMAT),
            lastAccessedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            enrollementDate: currentDate,
            lastAccessedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourseEnrollment', () => {
        const patchObject = Object.assign(
          {
            enrollementDate: currentDate.format(DATE_FORMAT),
            lastAccessedDate: currentDate.format(DATE_FORMAT),
          },
          new CourseEnrollment()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            enrollementDate: currentDate,
            lastAccessedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourseEnrollment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            enrollementDate: currentDate.format(DATE_FORMAT),
            lastAccessedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            enrollementDate: currentDate,
            lastAccessedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CourseEnrollment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseEnrollmentToCollectionIfMissing', () => {
        it('should add a CourseEnrollment to an empty array', () => {
          const courseEnrollment: ICourseEnrollment = { id: 123 };
          expectedResult = service.addCourseEnrollmentToCollectionIfMissing([], courseEnrollment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseEnrollment);
        });

        it('should not add a CourseEnrollment to an array that contains it', () => {
          const courseEnrollment: ICourseEnrollment = { id: 123 };
          const courseEnrollmentCollection: ICourseEnrollment[] = [
            {
              ...courseEnrollment,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, courseEnrollment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourseEnrollment to an array that doesn't contain it", () => {
          const courseEnrollment: ICourseEnrollment = { id: 123 };
          const courseEnrollmentCollection: ICourseEnrollment[] = [{ id: 456 }];
          expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, courseEnrollment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseEnrollment);
        });

        it('should add only unique CourseEnrollment to an array', () => {
          const courseEnrollmentArray: ICourseEnrollment[] = [{ id: 123 }, { id: 456 }, { id: 74865 }];
          const courseEnrollmentCollection: ICourseEnrollment[] = [{ id: 123 }];
          expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, ...courseEnrollmentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courseEnrollment: ICourseEnrollment = { id: 123 };
          const courseEnrollment2: ICourseEnrollment = { id: 456 };
          expectedResult = service.addCourseEnrollmentToCollectionIfMissing([], courseEnrollment, courseEnrollment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseEnrollment);
          expect(expectedResult).toContain(courseEnrollment2);
        });

        it('should accept null and undefined values', () => {
          const courseEnrollment: ICourseEnrollment = { id: 123 };
          expectedResult = service.addCourseEnrollmentToCollectionIfMissing([], null, courseEnrollment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseEnrollment);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
