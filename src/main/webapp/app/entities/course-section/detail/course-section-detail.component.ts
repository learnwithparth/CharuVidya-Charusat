import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseSection } from '../course-section.model';

@Component({
  selector: 'jhi-course-section-detail',
  templateUrl: './course-section-detail.component.html',
})
export class CourseSectionDetailComponent implements OnInit {
  courseSection: ICourseSection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseSection }) => {
      this.courseSection = courseSection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
