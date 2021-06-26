jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseSession, CourseSession } from '../course-session.model';
import { CourseSessionService } from '../service/course-session.service';

import { CourseSessionRoutingResolveService } from './course-session-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseSession routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseSessionRoutingResolveService;
    let service: CourseSessionService;
    let resultCourseSession: ICourseSession | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseSessionRoutingResolveService);
      service = TestBed.inject(CourseSessionService);
      resultCourseSession = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseSession returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseSession = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseSession).toEqual({ id: 123 });
      });

      it('should return new ICourseSession if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseSession = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseSession).toEqual(new CourseSession());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseSession = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseSession).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
