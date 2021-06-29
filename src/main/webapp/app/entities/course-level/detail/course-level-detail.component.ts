import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseLevel } from '../course-level.model';

@Component({
  selector: 'jhi-course-level-detail',
  templateUrl: './course-level-detail.component.html',
})
export class CourseLevelDetailComponent implements OnInit {
  courseLevel: ICourseLevel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseLevel }) => {
      this.courseLevel = courseLevel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
