import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICourseSession, CourseSession } from '../course-session.model';

import { CourseSessionService } from './course-session.service';

describe('Service Tests', () => {
  describe('CourseSession Service', () => {
    let service: CourseSessionService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseSession;
    let expectedResult: ICourseSession | ICourseSession[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseSessionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        sessionTitle: 'AAAAAAA',
        sessionDescription: 'AAAAAAA',
        sessionVideo: 'AAAAAAA',
        sessionDuration: currentDate,
        sessionOrder: 0,
        sessionResource: 'AAAAAAA',
        sessionLocation: 'AAAAAAA',
        isPreview: false,
        isDraft: false,
        isApproved: false,
        isPublished: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            sessionDuration: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CourseSession', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            sessionDuration: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            sessionDuration: currentDate,
          },
          returnedFromService
        );

        service.create(new CourseSession()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourseSession', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sessionTitle: 'BBBBBB',
            sessionDescription: 'BBBBBB',
            sessionVideo: 'BBBBBB',
            sessionDuration: currentDate.format(DATE_TIME_FORMAT),
            sessionOrder: 1,
            sessionResource: 'BBBBBB',
            sessionLocation: 'BBBBBB',
            isPreview: true,
            isDraft: true,
            isApproved: true,
            isPublished: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            sessionDuration: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourseSession', () => {
        const patchObject = Object.assign(
          {
            sessionDescription: 'BBBBBB',
            sessionResource: 'BBBBBB',
            sessionLocation: 'BBBBBB',
            isPreview: true,
            isDraft: true,
          },
          new CourseSession()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            sessionDuration: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourseSession', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sessionTitle: 'BBBBBB',
            sessionDescription: 'BBBBBB',
            sessionVideo: 'BBBBBB',
            sessionDuration: currentDate.format(DATE_TIME_FORMAT),
            sessionOrder: 1,
            sessionResource: 'BBBBBB',
            sessionLocation: 'BBBBBB',
            isPreview: true,
            isDraft: true,
            isApproved: true,
            isPublished: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            sessionDuration: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CourseSession', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseSessionToCollectionIfMissing', () => {
        it('should add a CourseSession to an empty array', () => {
          const courseSession: ICourseSession = { id: 123 };
          expectedResult = service.addCourseSessionToCollectionIfMissing([], courseSession);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseSession);
        });

        it('should not add a CourseSession to an array that contains it', () => {
          const courseSession: ICourseSession = { id: 123 };
          const courseSessionCollection: ICourseSession[] = [
            {
              ...courseSession,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseSessionToCollectionIfMissing(courseSessionCollection, courseSession);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourseSession to an array that doesn't contain it", () => {
          const courseSession: ICourseSession = { id: 123 };
          const courseSessionCollection: ICourseSession[] = [{ id: 456 }];
          expectedResult = service.addCourseSessionToCollectionIfMissing(courseSessionCollection, courseSession);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseSession);
        });

        it('should add only unique CourseSession to an array', () => {
          const courseSessionArray: ICourseSession[] = [{ id: 123 }, { id: 456 }, { id: 62575 }];
          const courseSessionCollection: ICourseSession[] = [{ id: 123 }];
          expectedResult = service.addCourseSessionToCollectionIfMissing(courseSessionCollection, ...courseSessionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courseSession: ICourseSession = { id: 123 };
          const courseSession2: ICourseSession = { id: 456 };
          expectedResult = service.addCourseSessionToCollectionIfMissing([], courseSession, courseSession2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseSession);
          expect(expectedResult).toContain(courseSession2);
        });

        it('should accept null and undefined values', () => {
          const courseSession: ICourseSession = { id: 123 };
          expectedResult = service.addCourseSessionToCollectionIfMissing([], null, courseSession, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseSession);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
