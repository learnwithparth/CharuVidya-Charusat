import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseCategory, CourseCategory } from '../course-category.model';
import { CourseCategoryService } from '../service/course-category.service';

@Injectable({ providedIn: 'root' })
export class CourseCategoryRoutingResolveService implements Resolve<ICourseCategory> {
  constructor(protected service: CourseCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courseCategory: HttpResponse<CourseCategory>) => {
          if (courseCategory.body) {
            return of(courseCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseCategory());
  }
}
