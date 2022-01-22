import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ICourse } from 'app/entities/course/course.model';
import { UserCourseService } from 'app/entities/user-pages/user-courses/user-courses.service';
import { HttpResponse } from '@angular/common/http';
import { faCalendarCheck, faEye, faUserFriends, faUserTie } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  faUserTie = faUserTie;
  faCalendarCheck = faCalendarCheck;
  faEye = faEye;
  faUserFriends = faUserFriends;

  account: Account | null = null;
  authSubscription?: Subscription;
  courses?: ICourse[] | null;
  overview = new Map<string, string>();

  constructor(protected userCourseService: UserCourseService, private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.loadAllCourses();
    this.getOverview();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(courseId: number | undefined): void {
    if (courseId !== undefined) {
      localStorage.setItem('course_Id', String(courseId));
    }
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  private getOverview(): void {
    this.userCourseService.getOverview().subscribe(
      res => {
        if (res.body) {
          this.overview = new Map(Object.entries(res.body));
        }
      },
      error => {
        console.error(error);
      }
    );
  }

  private loadAllCourses(): void {
    this.userCourseService.getCourses().subscribe(
      (res: HttpResponse<ICourse[]>) => {
        this.courses = res.body;
      },
      () => {
        window.alert('Error in fetching courses');
      }
    );
  }
}
