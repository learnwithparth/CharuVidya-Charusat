import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ICourseSection, getCourseSectionIdentifier } from '../../course-section/course-section.model';
import { ICourseSession } from 'app/entities/course-session/course-session.model';

export type EntityResponseType = HttpResponse<ICourseSection>;
export type EntityArrayResponseType = HttpResponse<ICourseSection[]>;

@Injectable({ providedIn: 'root' })
export class InstructorCourseSectionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-sections');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courseId: string, data: any): Observable<EntityResponseType> {
    return this.http.post<ICourseSection>(`api/course/${courseId}/course-sections`, data, { observe: 'response' });
  }

  update(courseSection: ICourseSection): Observable<EntityResponseType> {
    return this.http.put<ICourseSection>(`${this.resourceUrl}/${getCourseSectionIdentifier(courseSection) as number}`, courseSection, {
      observe: 'response',
    });
  }

  partialUpdate(courseSection: ICourseSection): Observable<EntityResponseType> {
    return this.http.patch<ICourseSection>(`${this.resourceUrl}/${getCourseSectionIdentifier(courseSection) as number}`, courseSection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseSection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(courseId: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICourseSection[]>(`api/course/${courseId}/course-sections`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourseSectionToCollectionIfMissing(
    courseSectionCollection: ICourseSection[],
    ...courseSectionsToCheck: (ICourseSection | null | undefined)[]
  ): ICourseSection[] {
    const courseSections: ICourseSection[] = courseSectionsToCheck.filter(isPresent);
    if (courseSections.length > 0) {
      const courseSectionCollectionIdentifiers = courseSectionCollection.map(
        courseSectionItem => getCourseSectionIdentifier(courseSectionItem)!
      );
      const courseSectionsToAdd = courseSections.filter(courseSectionItem => {
        const courseSectionIdentifier = getCourseSectionIdentifier(courseSectionItem);
        if (courseSectionIdentifier == null || courseSectionCollectionIdentifiers.includes(courseSectionIdentifier)) {
          return false;
        }
        courseSectionCollectionIdentifiers.push(courseSectionIdentifier);
        return true;
      });
      return [...courseSectionsToAdd, ...courseSectionCollection];
    }
    return courseSectionCollection;
  }

  getAllSectionsAndSessions(classId: string | null): Observable<HttpResponse<Map<ICourseSection, ICourseSession[]>>> {
    // if(classId!=null) {
    // let result:Map<ICourseSection,ICourseSession[]>=new Map();
    return this.http.get<Map<ICourseSection, ICourseSession[]>>('api/course/' + (classId as string) + '/course-sections-sessions', {
      observe: 'response',
    });
    // }
    // return null;
  }
}
