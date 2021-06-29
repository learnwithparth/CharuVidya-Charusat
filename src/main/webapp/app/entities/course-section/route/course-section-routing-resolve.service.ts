import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseSection, CourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';

@Injectable({ providedIn: 'root' })
export class CourseSectionRoutingResolveService implements Resolve<ICourseSection> {
  constructor(protected service: CourseSectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseSection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseSection: HttpResponse<CourseSection>) => {
          if (courseSection.body) {
            return of(courseSection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseSection());
  }
}
