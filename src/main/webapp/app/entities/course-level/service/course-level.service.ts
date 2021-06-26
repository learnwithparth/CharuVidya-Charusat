import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseLevel, getCourseLevelIdentifier } from '../course-level.model';

export type EntityResponseType = HttpResponse<ICourseLevel>;
export type EntityArrayResponseType = HttpResponse<ICourseLevel[]>;

@Injectable({ providedIn: 'root' })
export class CourseLevelService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-levels');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseLevel: ICourseLevel): Observable<EntityResponseType> {
    return this.http.post<ICourseLevel>(this.resourceUrl, courseLevel, { observe: 'response' });
  }

  update(courseLevel: ICourseLevel): Observable<EntityResponseType> {
    return this.http.put<ICourseLevel>(`${this.resourceUrl}/${getCourseLevelIdentifier(courseLevel) as number}`, courseLevel, {
      observe: 'response',
    });
  }

  partialUpdate(courseLevel: ICourseLevel): Observable<EntityResponseType> {
    return this.http.patch<ICourseLevel>(`${this.resourceUrl}/${getCourseLevelIdentifier(courseLevel) as number}`, courseLevel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseLevelToCollectionIfMissing(
    courseLevelCollection: ICourseLevel[],
    ...courseLevelsToCheck: (ICourseLevel | null | undefined)[]
  ): ICourseLevel[] {
    const courseLevels: ICourseLevel[] = courseLevelsToCheck.filter(isPresent);
    if (courseLevels.length > 0) {
      const courseLevelCollectionIdentifiers = courseLevelCollection.map(courseLevelItem => getCourseLevelIdentifier(courseLevelItem)!);
      const courseLevelsToAdd = courseLevels.filter(courseLevelItem => {
        const courseLevelIdentifier = getCourseLevelIdentifier(courseLevelItem);
        if (courseLevelIdentifier == null || courseLevelCollectionIdentifiers.includes(courseLevelIdentifier)) {
          return false;
        }
        courseLevelCollectionIdentifiers.push(courseLevelIdentifier);
        return true;
      });
      return [...courseLevelsToAdd, ...courseLevelCollection];
    }
    return courseLevelCollection;
  }
}
