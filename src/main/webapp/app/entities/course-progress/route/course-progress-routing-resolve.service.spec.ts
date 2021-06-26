jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseProgress, CourseProgress } from '../course-progress.model';
import { CourseProgressService } from '../service/course-progress.service';

import { CourseProgressRoutingResolveService } from './course-progress-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseProgress routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseProgressRoutingResolveService;
    let service: CourseProgressService;
    let resultCourseProgress: ICourseProgress | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseProgressRoutingResolveService);
      service = TestBed.inject(CourseProgressService);
      resultCourseProgress = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseProgress returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseProgress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseProgress).toEqual({ id: 123 });
      });

      it('should return new ICourseProgress if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseProgress = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseProgress).toEqual(new CourseProgress());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseProgress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseProgress).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
