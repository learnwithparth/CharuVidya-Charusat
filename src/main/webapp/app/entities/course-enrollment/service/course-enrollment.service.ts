import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseEnrollment, getCourseEnrollmentIdentifier } from '../course-enrollment.model';

export type EntityResponseType = HttpResponse<ICourseEnrollment>;
export type EntityArrayResponseType = HttpResponse<ICourseEnrollment[]>;

@Injectable({ providedIn: 'root' })
export class CourseEnrollmentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-enrollments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseEnrollment: ICourseEnrollment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseEnrollment);
    return this.http
      .post<ICourseEnrollment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseEnrollment: ICourseEnrollment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseEnrollment);
    return this.http
      .put<ICourseEnrollment>(`${this.resourceUrl}/${getCourseEnrollmentIdentifier(courseEnrollment) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(courseEnrollment: ICourseEnrollment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseEnrollment);
    return this.http
      .patch<ICourseEnrollment>(`${this.resourceUrl}/${getCourseEnrollmentIdentifier(courseEnrollment) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseEnrollment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseEnrollment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseEnrollmentToCollectionIfMissing(
    courseEnrollmentCollection: ICourseEnrollment[],
    ...courseEnrollmentsToCheck: (ICourseEnrollment | null | undefined)[]
  ): ICourseEnrollment[] {
    const courseEnrollments: ICourseEnrollment[] = courseEnrollmentsToCheck.filter(isPresent);
    if (courseEnrollments.length > 0) {
      const courseEnrollmentCollectionIdentifiers = courseEnrollmentCollection.map(
        courseEnrollmentItem => getCourseEnrollmentIdentifier(courseEnrollmentItem)!
      );
      const courseEnrollmentsToAdd = courseEnrollments.filter(courseEnrollmentItem => {
        const courseEnrollmentIdentifier = getCourseEnrollmentIdentifier(courseEnrollmentItem);
        if (courseEnrollmentIdentifier == null || courseEnrollmentCollectionIdentifiers.includes(courseEnrollmentIdentifier)) {
          return false;
        }
        courseEnrollmentCollectionIdentifiers.push(courseEnrollmentIdentifier);
        return true;
      });
      return [...courseEnrollmentsToAdd, ...courseEnrollmentCollection];
    }
    return courseEnrollmentCollection;
  }

  protected convertDateFromClient(courseEnrollment: ICourseEnrollment): ICourseEnrollment {
    return Object.assign({}, courseEnrollment, {
      enrollementDate: courseEnrollment.enrollementDate?.isValid() ? courseEnrollment.enrollementDate.format(DATE_FORMAT) : undefined,
      lastAccessedDate: courseEnrollment.lastAccessedDate?.isValid() ? courseEnrollment.lastAccessedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.enrollementDate = res.body.enrollementDate ? dayjs(res.body.enrollementDate) : undefined;
      res.body.lastAccessedDate = res.body.lastAccessedDate ? dayjs(res.body.lastAccessedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseEnrollment: ICourseEnrollment) => {
        courseEnrollment.enrollementDate = courseEnrollment.enrollementDate ? dayjs(courseEnrollment.enrollementDate) : undefined;
        courseEnrollment.lastAccessedDate = courseEnrollment.lastAccessedDate ? dayjs(courseEnrollment.lastAccessedDate) : undefined;
      });
    }
    return res;
  }
}
