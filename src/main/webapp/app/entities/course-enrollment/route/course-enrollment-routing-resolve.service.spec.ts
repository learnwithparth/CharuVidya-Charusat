jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseEnrollment, CourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';

import { CourseEnrollmentRoutingResolveService } from './course-enrollment-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseEnrollment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseEnrollmentRoutingResolveService;
    let service: CourseEnrollmentService;
    let resultCourseEnrollment: ICourseEnrollment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseEnrollmentRoutingResolveService);
      service = TestBed.inject(CourseEnrollmentService);
      resultCourseEnrollment = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseEnrollment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseEnrollment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseEnrollment).toEqual({ id: 123 });
      });

      it('should return new ICourseEnrollment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseEnrollment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseEnrollment).toEqual(new CourseEnrollment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseEnrollment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseEnrollment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
