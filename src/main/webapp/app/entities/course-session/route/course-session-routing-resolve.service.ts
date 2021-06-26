import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseSession, CourseSession } from '../course-session.model';
import { CourseSessionService } from '../service/course-session.service';

@Injectable({ providedIn: 'root' })
export class CourseSessionRoutingResolveService implements Resolve<ICourseSession> {
  constructor(protected service: CourseSessionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseSession> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseSession: HttpResponse<CourseSession>) => {
          if (courseSession.body) {
            return of(courseSession.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseSession());
  }
}
