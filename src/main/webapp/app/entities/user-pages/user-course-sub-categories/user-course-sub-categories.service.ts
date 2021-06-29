import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

import { ICourseCategory } from '../../course-category/course-category.model';

export type EntityResponseType = HttpResponse<ICourseCategory>;
export type EntityArrayResponseType = HttpResponse<ICourseCategory[]>;

@Injectable({ providedIn: 'root' })
export class UserCourseSubCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(id: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICourseCategory[]>(`api/course-category/sub-categories/${id}`, { observe: 'response' });
  }
}
