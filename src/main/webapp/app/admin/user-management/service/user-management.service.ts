import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Pagination } from 'app/core/request/request.model';
import { IUser, User } from '../user-management.model';
import { CourseCategory } from 'app/entities/course-category/course-category.model';

@Injectable({ providedIn: 'root' })
export class UserManagementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/admin/users');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(user: IUser): Observable<IUser> {
    return this.http.post<IUser>(this.resourceUrl, user);
  }

  update(user: IUser): Observable<IUser> {
    return this.http.put<IUser>(this.resourceUrl, user);
  }

  find(login: string): Observable<IUser> {
    return this.http.get<IUser>(`${this.resourceUrl}/${login}`);
  }

  query(req?: Pagination): Observable<HttpResponse<IUser[]>> {
    const options = createRequestOption(req);
    return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(login: string): Observable<{}> {
    return this.http.delete(`${this.resourceUrl}/${login}`);
  }

  authorities(): Observable<string[]> {
    return this.http.get<string[]>(this.applicationConfigService.getEndpointFor('api/authorities'));
  }

  getCategories(): Observable<HttpResponse<any>> {
    return this.http.get<CourseCategory[]>(`/api/course-category/sub-categories`, { observe: 'response' });
  }

  getCourseCategoryOfReviewer(userId: string): Observable<HttpResponse<any>> {
    return this.http.get<CourseCategory[]>(`/api/course-category/sub-category/user/${userId}`, { observe: 'response' });
  }

  setReviewerCategory(user: User, reviewerCategories: CourseCategory[]): Observable<{}> {
    return this.http.post(`/api/admin/reviewer/setReviewerCategory`, { user, reviewerCategories });
  }
}
