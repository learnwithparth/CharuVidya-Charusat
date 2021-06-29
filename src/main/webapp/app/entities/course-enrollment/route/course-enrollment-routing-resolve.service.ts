import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseEnrollment, CourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';

@Injectable({ providedIn: 'root' })
export class CourseEnrollmentRoutingResolveService implements Resolve<ICourseEnrollment> {
  constructor(protected service: CourseEnrollmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseEnrollment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseEnrollment: HttpResponse<CourseEnrollment>) => {
          if (courseEnrollment.body) {
            return of(courseEnrollment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseEnrollment());
  }
}
