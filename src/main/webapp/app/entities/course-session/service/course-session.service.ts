import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseSession, getCourseSessionIdentifier } from '../course-session.model';

export type EntityResponseType = HttpResponse<ICourseSession>;
export type EntityArrayResponseType = HttpResponse<ICourseSession[]>;

@Injectable({ providedIn: 'root' })
export class CourseSessionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-sessions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseSession: ICourseSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseSession);
    return this.http
      .post<ICourseSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseSession: ICourseSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseSession);
    return this.http
      .put<ICourseSession>(`${this.resourceUrl}/${getCourseSessionIdentifier(courseSession) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(courseSession: ICourseSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseSession);
    return this.http
      .patch<ICourseSession>(`${this.resourceUrl}/${getCourseSessionIdentifier(courseSession) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseSessionToCollectionIfMissing(
    courseSessionCollection: ICourseSession[],
    ...courseSessionsToCheck: (ICourseSession | null | undefined)[]
  ): ICourseSession[] {
    const courseSessions: ICourseSession[] = courseSessionsToCheck.filter(isPresent);
    if (courseSessions.length > 0) {
      const courseSessionCollectionIdentifiers = courseSessionCollection.map(
        courseSessionItem => getCourseSessionIdentifier(courseSessionItem)!
      );
      const courseSessionsToAdd = courseSessions.filter(courseSessionItem => {
        const courseSessionIdentifier = getCourseSessionIdentifier(courseSessionItem);
        if (courseSessionIdentifier == null || courseSessionCollectionIdentifiers.includes(courseSessionIdentifier)) {
          return false;
        }
        courseSessionCollectionIdentifiers.push(courseSessionIdentifier);
        return true;
      });
      return [...courseSessionsToAdd, ...courseSessionCollection];
    }
    return courseSessionCollection;
  }

  protected convertDateFromClient(courseSession: ICourseSession): ICourseSession {
    return Object.assign({}, courseSession, {
      sessionDuration: courseSession.sessionDuration?.isValid() ? courseSession.sessionDuration.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.sessionDuration = res.body.sessionDuration ? dayjs(res.body.sessionDuration) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseSession: ICourseSession) => {
        courseSession.sessionDuration = courseSession.sessionDuration ? dayjs(courseSession.sessionDuration) : undefined;
      });
    }
    return res;
  }
}
