import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { UserCourseCategoryService } from 'app/entities/user-pages/user-course-category/user-course-category.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-user-course-category',
  templateUrl: './user-course-category.component.html',
  styleUrls: ['./user-course-category.component.scss'],
})
export class UserCourseCategoryComponent implements OnInit {
  courseCategories?: ICourseCategory[] | null;
  isActive = false;
  map: Map<string, number> = new Map<string, number>();

  constructor(
    protected userCourseCategoryService: UserCourseCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAllCategories();
  }

  private loadAllCategories(): void {
    this.userCourseCategoryService.query().subscribe(
      (res: HttpResponse<ICourseCategory[]>) => {
        this.courseCategories = res.body;
        this.loadCourseCount();
      },
      () => {
        window.alert('Error in fetching parent categories');
      }
    );
  }

  private loadCourseCount(): void {
    this.userCourseCategoryService.courseCount().subscribe((res: any) => {
      this.map = new Map(Object.entries(res.body));
    });
  }
}
