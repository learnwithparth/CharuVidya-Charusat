import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpResponse } from '@angular/common/http';
import { observable, Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class NavbarService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/course-category');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  public getParentCategoryAndSubCategoryMap(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.resourceUrl}/parent-categories/sub-categories`, { observe: 'response' });
  }

  public getSubCategoriesAndCourses(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.resourceUrl}/sub-categories/courses`, { observe: 'response' });
  }
}
