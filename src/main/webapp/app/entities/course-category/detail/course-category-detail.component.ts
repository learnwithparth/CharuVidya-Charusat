import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseCategory } from '../course-category.model';

@Component({
  selector: 'jhi-course-category-detail',
  templateUrl: './course-category-detail.component.html',
})
export class CourseCategoryDetailComponent implements OnInit {
  courseCategory: ICourseCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseCategory }) => {
      this.courseCategory = courseCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
