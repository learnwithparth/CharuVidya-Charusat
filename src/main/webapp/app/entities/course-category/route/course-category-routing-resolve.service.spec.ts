jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseCategory, CourseCategory } from '../course-category.model';
import { CourseCategoryService } from '../service/course-category.service';

import { CourseCategoryRoutingResolveService } from './course-category-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseCategory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseCategoryRoutingResolveService;
    let service: CourseCategoryService;
    let resultCourseCategory: ICourseCategory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseCategoryRoutingResolveService);
      service = TestBed.inject(CourseCategoryService);
      resultCourseCategory = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseCategory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseCategory).toEqual({ id: 123 });
      });

      it('should return new ICourseCategory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseCategory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseCategory).toEqual(new CourseCategory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseCategory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
