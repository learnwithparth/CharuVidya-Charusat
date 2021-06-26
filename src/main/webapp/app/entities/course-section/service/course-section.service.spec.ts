import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICourseSection, CourseSection } from '../course-section.model';

import { CourseSectionService } from './course-section.service';

describe('Service Tests', () => {
  describe('CourseSection Service', () => {
    let service: CourseSectionService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseSection;
    let expectedResult: ICourseSection | ICourseSection[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourseSectionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        sectionTitle: 'AAAAAAA',
        sectionDescription: 'AAAAAAA',
        sectionOrder: 0,
        isDraft: false,
        isApproved: false,
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

      it('should create a CourseSection', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CourseSection()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourseSection', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sectionTitle: 'BBBBBB',
            sectionDescription: 'BBBBBB',
            sectionOrder: 1,
            isDraft: true,
            isApproved: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourseSection', () => {
        const patchObject = Object.assign(
          {
            sectionDescription: 'BBBBBB',
            sectionOrder: 1,
            isApproved: true,
          },
          new CourseSection()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourseSection', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sectionTitle: 'BBBBBB',
            sectionDescription: 'BBBBBB',
            sectionOrder: 1,
            isDraft: true,
            isApproved: true,
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

      it('should delete a CourseSection', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourseSectionToCollectionIfMissing', () => {
        it('should add a CourseSection to an empty array', () => {
          const courseSection: ICourseSection = { id: 123 };
          expectedResult = service.addCourseSectionToCollectionIfMissing([], courseSection);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseSection);
        });

        it('should not add a CourseSection to an array that contains it', () => {
          const courseSection: ICourseSection = { id: 123 };
          const courseSectionCollection: ICourseSection[] = [
            {
              ...courseSection,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, courseSection);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourseSection to an array that doesn't contain it", () => {
          const courseSection: ICourseSection = { id: 123 };
          const courseSectionCollection: ICourseSection[] = [{ id: 456 }];
          expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, courseSection);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseSection);
        });

        it('should add only unique CourseSection to an array', () => {
          const courseSectionArray: ICourseSection[] = [{ id: 123 }, { id: 456 }, { id: 4491 }];
          const courseSectionCollection: ICourseSection[] = [{ id: 123 }];
          expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, ...courseSectionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courseSection: ICourseSection = { id: 123 };
          const courseSection2: ICourseSection = { id: 456 };
          expectedResult = service.addCourseSectionToCollectionIfMissing([], courseSection, courseSection2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courseSection);
          expect(expectedResult).toContain(courseSection2);
        });

        it('should accept null and undefined values', () => {
          const courseSection: ICourseSection = { id: 123 };
          expectedResult = service.addCourseSectionToCollectionIfMissing([], null, courseSection, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courseSection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
