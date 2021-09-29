import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseProgress, getCourseProgressIdentifier } from '../course-progress.model';

export type EntityResponseType = HttpResponse<ICourseProgress>;
export type EntityArrayResponseType = HttpResponse<ICourseProgress[]>;

@Injectable({ providedIn: 'root' })
export class CourseProgressService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-progresses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseProgress: ICourseProgress): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseProgress);
    return this.http
      .post<ICourseProgress>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseProgress: ICourseProgress): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseProgress);
    return this.http
      .put<ICourseProgress>(`${this.resourceUrl}/${getCourseProgressIdentifier(courseProgress) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(courseProgress: ICourseProgress): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseProgress);
    return this.http
      .patch<ICourseProgress>(`${this.resourceUrl}/${getCourseProgressIdentifier(courseProgress) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseProgress>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseProgress[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseProgressToCollectionIfMissing(
    courseProgressCollection: ICourseProgress[],
    ...courseProgressesToCheck: (ICourseProgress | null | undefined)[]
  ): ICourseProgress[] {
    const courseProgresses: ICourseProgress[] = courseProgressesToCheck.filter(isPresent);
    if (courseProgresses.length > 0) {
      const courseProgressCollectionIdentifiers = courseProgressCollection.map(
        courseProgressItem => getCourseProgressIdentifier(courseProgressItem)!
      );
      const courseProgressesToAdd = courseProgresses.filter(courseProgressItem => {
        const courseProgressIdentifier = getCourseProgressIdentifier(courseProgressItem);
        if (courseProgressIdentifier == null || courseProgressCollectionIdentifiers.includes(courseProgressIdentifier)) {
          return false;
        }
        courseProgressCollectionIdentifiers.push(courseProgressIdentifier);
        return true;
      });
      return [...courseProgressesToAdd, ...courseProgressCollection];
    }
    return courseProgressCollection;
  }
  findBySessionId(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseProgress>(`${this.resourceUrl}/currentUserWatchTime/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  updateUserWatchTime(courseProgress: ICourseProgress): void {
    // courseProgress.id=1;
    // console.log(courseProgress);
    this.http
      .post<ICourseProgress>(this.resourceUrl + '/updateUserWatchTime', courseProgress, { observe: 'response' })
      .subscribe(
        res => {
          // console.log(res);
        },
        err => {
          // console.log(err);
        }
      );
    // console.log(x);
  }

  protected convertDateFromClient(courseProgress: ICourseProgress): ICourseProgress {
    return Object.assign({}, courseProgress, {
      watchSeconds: courseProgress.watchSeconds ? courseProgress.watchSeconds : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.watchSeconds = res.body.watchSeconds ? res.body.watchSeconds : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseProgress: ICourseProgress) => {
        courseProgress.watchSeconds = courseProgress.watchSeconds ? courseProgress.watchSeconds : undefined;
      });
    }
    return res;
  }
}
