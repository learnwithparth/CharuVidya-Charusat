import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseSession } from '../course-session.model';

@Component({
  selector: 'jhi-course-session-detail',
  templateUrl: './course-session-detail.component.html',
})
export class CourseSessionDetailComponent implements OnInit {
  courseSession: ICourseSession | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseSession }) => {
      this.courseSession = courseSession;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
