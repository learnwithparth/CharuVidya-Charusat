jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseLevel, CourseLevel } from '../course-level.model';
import { CourseLevelService } from '../service/course-level.service';

import { CourseLevelRoutingResolveService } from './course-level-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseLevel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseLevelRoutingResolveService;
    let service: CourseLevelService;
    let resultCourseLevel: ICourseLevel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseLevelRoutingResolveService);
      service = TestBed.inject(CourseLevelService);
      resultCourseLevel = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseLevel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseLevel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseLevel).toEqual({ id: 123 });
      });

      it('should return new ICourseLevel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseLevel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseLevel).toEqual(new CourseLevel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseLevel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseLevel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
