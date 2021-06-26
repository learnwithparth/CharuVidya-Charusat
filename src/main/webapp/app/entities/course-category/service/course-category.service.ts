import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseCategory, getCourseCategoryIdentifier } from '../course-category.model';

export type EntityResponseType = HttpResponse<ICourseCategory>;
export type EntityArrayResponseType = HttpResponse<ICourseCategory[]>;

@Injectable({ providedIn: 'root' })
export class CourseCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseCategory: ICourseCategory): Observable<EntityResponseType> {
    return this.http.post<ICourseCategory>(this.resourceUrl, courseCategory, { observe: 'response' });
  }

  update(courseCategory: ICourseCategory): Observable<EntityResponseType> {
    return this.http.put<ICourseCategory>(`${this.resourceUrl}/${getCourseCategoryIdentifier(courseCategory) as number}`, courseCategory, {
      observe: 'response',
    });
  }

  partialUpdate(courseCategory: ICourseCategory): Observable<EntityResponseType> {
    return this.http.patch<ICourseCategory>(
      `${this.resourceUrl}/${getCourseCategoryIdentifier(courseCategory) as number}`,
      courseCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseCategoryToCollectionIfMissing(
    courseCategoryCollection: ICourseCategory[],
    ...courseCategoriesToCheck: (ICourseCategory | null | undefined)[]
  ): ICourseCategory[] {
    const courseCategories: ICourseCategory[] = courseCategoriesToCheck.filter(isPresent);
    if (courseCategories.length > 0) {
      const courseCategoryCollectionIdentifiers = courseCategoryCollection.map(
        courseCategoryItem => getCourseCategoryIdentifier(courseCategoryItem)!
      );
      const courseCategoriesToAdd = courseCategories.filter(courseCategoryItem => {
        const courseCategoryIdentifier = getCourseCategoryIdentifier(courseCategoryItem);
        if (courseCategoryIdentifier == null || courseCategoryCollectionIdentifiers.includes(courseCategoryIdentifier)) {
          return false;
        }
        courseCategoryCollectionIdentifiers.push(courseCategoryIdentifier);
        return true;
      });
      return [...courseCategoriesToAdd, ...courseCategoryCollection];
    }
    return courseCategoryCollection;
  }
}
