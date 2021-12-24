import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { ICourseVisitModel } from 'app/entities/course-visit/course-visit.model';

export type EntityResponseType = HttpResponse<ICourseVisitModel>;
export type EntityArrayResponseType = HttpResponse<ICourseVisitModel[]>;
@Injectable({
  providedIn: 'root',
})
export class CourseVisitService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/admin/course-visits');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getCourseVisits(): Observable<EntityArrayResponseType> {
    return this.http.get<ICourseVisitModel[]>(this.resourceUrl, { observe: 'response' });
  }
}
