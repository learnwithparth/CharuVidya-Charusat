import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseLevel, CourseLevel } from '../course-level.model';
import { CourseLevelService } from '../service/course-level.service';

@Injectable({ providedIn: 'root' })
export class CourseLevelRoutingResolveService implements Resolve<ICourseLevel> {
  constructor(protected service: CourseLevelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseLevel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseLevel: HttpResponse<CourseLevel>) => {
          if (courseLevel.body) {
            return of(courseLevel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseLevel());
  }
}
