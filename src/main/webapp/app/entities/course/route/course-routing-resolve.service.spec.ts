jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourse, Course } from '../course.model';
import { CourseService } from '../service/course.service';

import { CourseRoutingResolveService } from './course-routing-resolve.service';

describe('Service Tests', () => {
  describe('Course routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseRoutingResolveService;
    let service: CourseService;
    let resultCourse: ICourse | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseRoutingResolveService);
      service = TestBed.inject(CourseService);
      resultCourse = undefined;
    });

    describe('resolve', () => {
      it('should return ICourse returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourse = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourse).toEqual({ id: 123 });
      });

      it('should return new ICourse if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourse = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourse).toEqual(new Course());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourse = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourse).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
