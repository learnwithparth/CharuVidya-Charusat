import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICourseCategory, CourseCategory } from '../course-category.model';

import { CourseCategoryService } from './course-category.service';

describe('Service Tests', () => {
  describe('CourseCategory Service', () => {
    let service: CourseCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseCategory;
    let expectedResult: ICourseCategory | ICourseCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        courseCategoryTitle: 'AAAAAAA',
        logo: 'AAAAAAA',
        isParent: 0,
        parentId: 0,
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

      it('should create a CourseCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CourseCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourseCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            courseCategoryTitle: 'BBBBBB',
            logo: 'BBBBBB',
            isParent: 1,
            parentId: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourseCategory', () => {
        const patchObject = Object.assign({}, new CourseCategory());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourseCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            courseCategoryTitle: 'BBBBBB',
            logo: 'BBBBBB',
            isParent: 1,
            parentId: 1,
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

      it('should delete a CourseCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseCategoryToCollectionIfMissing', () => {
        it('should add a CourseCategory to an empty array', () => {
          const courseCategory: ICourseCategory = { id: 123 };
          expectedResult = service.addCourseCategoryToCollectionIfMissing([], courseCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseCategory);
        });

        it('should not add a CourseCategory to an array that contains it', () => {
          const courseCategory: ICourseCategory = { id: 123 };
          const courseCategoryCollection: ICourseCategory[] = [
            {
              ...courseCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseCategoryToCollectionIfMissing(courseCategoryCollection, courseCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourseCategory to an array that doesn't contain it", () => {
          const courseCategory: ICourseCategory = { id: 123 };
          const courseCategoryCollection: ICourseCategory[] = [{ id: 456 }];
          expectedResult = service.addCourseCategoryToCollectionIfMissing(courseCategoryCollection, courseCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseCategory);
        });

        it('should add only unique CourseCategory to an array', () => {
          const courseCategoryArray: ICourseCategory[] = [{ id: 123 }, { id: 456 }, { id: 16132 }];
          const courseCategoryCollection: ICourseCategory[] = [{ id: 123 }];
          expectedResult = service.addCourseCategoryToCollectionIfMissing(courseCategoryCollection, ...courseCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courseCategory: ICourseCategory = { id: 123 };
          const courseCategory2: ICourseCategory = { id: 456 };
          expectedResult = service.addCourseCategoryToCollectionIfMissing([], courseCategory, courseCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseCategory);
          expect(expectedResult).toContain(courseCategory2);
        });

        it('should accept null and undefined values', () => {
          const courseCategory: ICourseCategory = { id: 123 };
          expectedResult = service.addCourseCategoryToCollectionIfMissing([], null, courseCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseCategory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
