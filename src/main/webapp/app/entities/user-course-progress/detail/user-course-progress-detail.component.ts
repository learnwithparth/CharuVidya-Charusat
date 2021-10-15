import { Component, OnInit } from '@angular/core';
import { IUserCourseProgress } from 'app/entities/user-course-progress/model/user-course-progress.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-user-course-progress-detail',
  templateUrl: './user-course-progress-detail.component.html',
  styleUrls: ['./user-course-progress-detail.component.scss'],
})
export class UserCourseProgressDetailComponent implements OnInit {
  userCourseProgress: IUserCourseProgress | null = null;
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    console.log(this.activatedRoute);
    this.activatedRoute.data.subscribe(({ userCourseProgress }) => {
      this.userCourseProgress = userCourseProgress;
      console.log(userCourseProgress);
    });
    console.log(this.userCourseProgress);
  }

  previousState(): void {
    window.history.back();
  }
}
