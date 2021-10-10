import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { CourseProgress, ICourseProgress } from 'app/entities/course-progress/course-progress.model';
import { IUserCourseProgress } from 'app/entities/user-course-progress/model/user-course-progress.model';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { EntityResponseType } from 'app/entities/course-progress/service/course-progress.service';

@Injectable({
  providedIn: 'root',
})
export class UserCourseProgressService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/user-course-progress');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  updateUserCourseProgress(userCourseProgress: IUserCourseProgress): void {
    this.http
      .post<IUserCourseProgress>(this.resourceUrl, userCourseProgress, { observe: 'response' })
      .subscribe();
  }
  getCourseProgress(userCourseProgress: IUserCourseProgress): Observable<HttpResponse<ICourseProgress>> | null {
    let courseProgress;
    return this.http.post<ICourseProgress>(this.resourceUrl + '/getCourseProgress', userCourseProgress, { observe: 'response' });
    // return null;
  }
  find(id: number): Observable<HttpResponse<IUserCourseProgress>> {
    return this.http.get<IUserCourseProgress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
