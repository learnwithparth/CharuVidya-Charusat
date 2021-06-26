import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseProgress } from '../course-progress.model';

@Component({
  selector: 'jhi-course-progress-detail',
  templateUrl: './course-progress-detail.component.html',
})
export class CourseProgressDetailComponent implements OnInit {
  courseProgress: ICourseProgress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseProgress }) => {
      this.courseProgress = courseProgress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
