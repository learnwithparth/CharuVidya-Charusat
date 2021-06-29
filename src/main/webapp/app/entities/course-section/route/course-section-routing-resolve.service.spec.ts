jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourseSection, CourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';

import { CourseSectionRoutingResolveService } from './course-section-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourseSection routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourseSectionRoutingResolveService;
    let service: CourseSectionService;
    let resultCourseSection: ICourseSection | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourseSectionRoutingResolveService);
      service = TestBed.inject(CourseSectionService);
      resultCourseSection = undefined;
    });

    describe('resolve', () => {
      it('should return ICourseSection returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseSection = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseSection).toEqual({ id: 123 });
      });

      it('should return new ICourseSection if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseSection = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourseSection).toEqual(new CourseSection());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourseSection = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourseSection).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
