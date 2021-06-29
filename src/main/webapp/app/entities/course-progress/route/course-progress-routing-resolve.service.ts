import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseProgress, CourseProgress } from '../course-progress.model';
import { CourseProgressService } from '../service/course-progress.service';

@Injectable({ providedIn: 'root' })
export class CourseProgressRoutingResolveService implements Resolve<ICourseProgress> {
  constructor(protected service: CourseProgressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseProgress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseProgress: HttpResponse<CourseProgress>) => {
          if (courseProgress.body) {
            return of(courseProgress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseProgress());
  }
}
