import { Component, OnInit } from '@angular/core';
import { UserCourseCategoryService } from 'app/entities/user-pages/user-course-category/user-course-category.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserCourseSubCategoryService } from 'app/entities/user-pages/user-course-sub-categories/user-course-sub-categories.service';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-user-course-sub-categories',
  templateUrl: './user-course-sub-categories.component.html',
  styleUrls: ['./user-course-sub-categories.component.scss'],
})
export class UserCourseSubCategoriesComponent implements OnInit {
  courseCategories?: ICourseCategory[] | null;
  categoryId?: string | null;
  map: Map<string, number> = new Map<string, number>();

  constructor(
    protected userCourseSubCategoryService: UserCourseSubCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.map = new Map<string, number>([]);
  }

  ngOnInit(): void {
    this.loadAllSubCategories();
  }

  private loadAllSubCategories(): void {
    const hasParentId: boolean = this.activatedRoute.snapshot.paramMap.has('parentId');
    if (hasParentId) {
      this.categoryId = this.activatedRoute.snapshot.paramMap.get('parentId');
    }
    if (this.categoryId !== null && this.categoryId !== undefined) {
      this.userCourseSubCategoryService.query(this.categoryId).subscribe(
        (res: HttpResponse<ICourseCategory[]>) => {
          this.courseCategories = res.body;
          this.loadCourseCount();
        },
        () => {
          window.alert('Error in fetching parent categories');
        }
      );
    }
  }

  private loadCourseCount(): void {
    this.userCourseSubCategoryService.courseCount(this.categoryId!).subscribe(res => {
      this.map = new Map(Object.entries(res.body));
    });
  }
}
