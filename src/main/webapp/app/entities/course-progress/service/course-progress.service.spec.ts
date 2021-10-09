import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICourseProgress, CourseProgress } from '../course-progress.model';

import { CourseProgressService } from './course-progress.service';

describe('Service Tests', () => {
  describe('CourseProgress Service', () => {
    let service: CourseProgressService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseProgress;
    let expectedResult: ICourseProgress | ICourseProgress[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseProgressService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        completed: false,
        watchSeconds: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            watchSeconds: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CourseProgress', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            watchSeconds: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            watchSeconds: currentDate,
          },
          returnedFromService
        );

        service.create(new CourseProgress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourseProgress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            completed: true,
            watchSeconds: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            watchSeconds: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourseProgress', () => {
        const patchObject = Object.assign(
          {
            watchSeconds: currentDate.format(DATE_TIME_FORMAT),
          },
          new CourseProgress()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            watchSeconds: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourseProgress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            completed: true,
            watchSeconds: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            watchSeconds: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CourseProgress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseProgressToCollectionIfMissing', () => {
        it('should add a CourseProgress to an empty array', () => {
          const courseProgress: ICourseProgress = { id: 123 };
          expectedResult = service.addCourseProgressToCollectionIfMissing([], courseProgress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseProgress);
        });

        it('should not add a CourseProgress to an array that contains it', () => {
          const courseProgress: ICourseProgress = { id: 123 };
          const courseProgressCollection: ICourseProgress[] = [
            {
              ...courseProgress,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseProgressToCollectionIfMissing(courseProgressCollection, courseProgress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourseProgress to an array that doesn't contain it", () => {
          const courseProgress: ICourseProgress = { id: 123 };
          const courseProgressCollection: ICourseProgress[] = [{ id: 456 }];
          expectedResult = service.addCourseProgressToCollectionIfMissing(courseProgressCollection, courseProgress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseProgress);
        });

        it('should add only unique CourseProgress to an array', () => {
          const courseProgressArray: ICourseProgress[] = [{ id: 123 }, { id: 456 }, { id: 76986 }];
          const courseProgressCollection: ICourseProgress[] = [{ id: 123 }];
          expectedResult = service.addCourseProgressToCollectionIfMissing(courseProgressCollection, ...courseProgressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courseProgress: ICourseProgress = { id: 123 };
          const courseProgress2: ICourseProgress = { id: 456 };
          expectedResult = service.addCourseProgressToCollectionIfMissing([], courseProgress, courseProgress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseProgress);
          expect(expectedResult).toContain(courseProgress2);
        });

        it('should accept null and undefined values', () => {
          const courseProgress: ICourseProgress = { id: 123 };
          expectedResult = service.addCourseProgressToCollectionIfMissing([], null, courseProgress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseProgress);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
