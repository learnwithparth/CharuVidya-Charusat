import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ICourseSession, getCourseSessionIdentifier } from '../../course-session/course-session.model';

export type EntityResponseType = HttpResponse<ICourseSession>;
export type EntityArrayResponseType = HttpResponse<ICourseSession[]>;

@Injectable({ providedIn: 'root' })
export class InstructorCourseSessionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-sessions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseId: string, courseSectionId: string, data: FormData): Observable<EntityResponseType> {
    return this.http.post<ICourseSession>(`api/course/${courseId}/course-section/${courseSectionId}/course-session`, data, {
      observe: 'response',
    });
  }

  update(courseSession: ICourseSession): Observable<EntityResponseType> {
    return this.http.put<ICourseSession>(`${this.resourceUrl}/${getCourseSessionIdentifier(courseSession) as number}`, courseSession, {
      observe: 'response',
    });
  }

  partialUpdate(courseSession: ICourseSession): Observable<EntityResponseType> {
    return this.http.patch<ICourseSession>(`${this.resourceUrl}/${getCourseSessionIdentifier(courseSession) as number}`, courseSession, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICourseSession>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(courseId: string, courseSectionId: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICourseSession[]>(`api/course/${courseId}/course-section/${courseSectionId}/course-sessions`, {
      observe: 'response',
    });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  approveSession(session: ICourseSession): Observable<HttpResponse<any>> {
    return this.http.post(`${this.resourceUrl}/approve`, session, { observe: 'response' });
  }
}
