import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IUser } from 'app/admin/user-management/user-management.model';
import { Observable } from 'rxjs';
import { Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class AssignCategoryReviewerService {
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getReviewerByCategory(courseCategoryId: number): Observable<HttpResponse<any>> {
    return this.http.get<IUser[]>(`/api/course-category/${courseCategoryId}/get-reviewers`, { observe: 'response' });
  }

  getInstructors(): Observable<HttpResponse<any>> {
    return this.http.get<IUser[]>(`/api/admin/users-by-authority?authority=ROLE_REVIEWER`, { observe: 'response' });
  }

  setReviewersByCategory(selectedReviewers: any, id: number): Observable<HttpResponse<any>> {
    return this.http.post(`/api/course-category/${id}/set-reviewers`, selectedReviewers, { observe: 'response' });
  }
}
