import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseEnrollment } from '../course-enrollment.model';

@Component({
  selector: 'jhi-course-enrollment-detail',
  templateUrl: './course-enrollment-detail.component.html',
})
export class CourseEnrollmentDetailComponent implements OnInit {
  courseEnrollment: ICourseEnrollment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseEnrollment }) => {
      this.courseEnrollment = courseEnrollment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
