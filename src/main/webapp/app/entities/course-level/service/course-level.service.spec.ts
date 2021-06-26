import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICourseLevel, CourseLevel } from '../course-level.model';

import { CourseLevelService } from './course-level.service';

describe('Service Tests', () => {
  describe('CourseLevel Service', () => {
    let service: CourseLevelService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseLevel;
    let expectedResult: ICourseLevel | ICourseLevel[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseLevelService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        level: 'AAAAAAA',
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CourseLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CourseLevel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourseLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            level: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourseLevel', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
          },
          new CourseLevel()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourseLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            level: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CourseLevel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseLevelToCollectionIfMissing', () => {
        it('should add a CourseLevel to an empty array', () => {
          const courseLevel: ICourseLevel = { id: 123 };
          expectedResult = service.addCourseLevelToCollectionIfMissing([], courseLevel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseLevel);
        });

        it('should not add a CourseLevel to an array that contains it', () => {
          const courseLevel: ICourseLevel = { id: 123 };
          const courseLevelCollection: ICourseLevel[] = [
            {
              ...courseLevel,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseLevelToCollectionIfMissing(courseLevelCollection, courseLevel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourseLevel to an array that doesn't contain it", () => {
          const courseLevel: ICourseLevel = { id: 123 };
          const courseLevelCollection: ICourseLevel[] = [{ id: 456 }];
          expectedResult = service.addCourseLevelToCollectionIfMissing(courseLevelCollection, courseLevel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseLevel);
        });

        it('should add only unique CourseLevel to an array', () => {
          const courseLevelArray: ICourseLevel[] = [{ id: 123 }, { id: 456 }, { id: 77042 }];
          const courseLevelCollection: ICourseLevel[] = [{ id: 123 }];
          expectedResult = service.addCourseLevelToCollectionIfMissing(courseLevelCollection, ...courseLevelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courseLevel: ICourseLevel = { id: 123 };
          const courseLevel2: ICourseLevel = { id: 456 };
          expectedResult = service.addCourseLevelToCollectionIfMissing([], courseLevel, courseLevel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseLevel);
          expect(expectedResult).toContain(courseLevel2);
        });

        it('should accept null and undefined values', () => {
          const courseLevel: ICourseLevel = { id: 123 };
          expectedResult = service.addCourseLevelToCollectionIfMissing([], null, courseLevel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseLevel);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
