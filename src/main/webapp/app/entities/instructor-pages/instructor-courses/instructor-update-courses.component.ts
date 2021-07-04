import { Component, OnInit } from '@angular/core';
import { ICourse } from 'app/entities/course/course.model';

import { InstructorCoursesService } from 'app/entities/instructor-pages/instructor-courses/instructor-courses.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { map } from 'rxjs/operators';
import { ICourseLevel } from 'app/entities/course-level/course-level.model';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { IUser } from 'app/entities/user/user.model';
import { CourseLevelService } from 'app/entities/course-level/service/course-level.service';
import { CourseCategoryService } from 'app/entities/course-category/service/course-category.service';
import { type } from 'os';

@Component({
  selector: 'jhi-instructor-update-courses',
  templateUrl: './instructor-update-courses.component.html',
})
export class InstructorUpdateCoursesComponent implements OnInit {
  courses?: ICourse[] | null;
  courseLevelsSharedCollection: ICourseLevel[] = [];
  courseCategories: ICourseCategory[] = [];
  courseParentCategoriesSharedCollection: ICourseCategory[] = [];
  courseSubCategoriesSharedCollection: ICourseCategory[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    courseTitle: [null, [Validators.required, Validators.maxLength(600)]],
    courseDescription: [null, [Validators.required, Validators.maxLength(255)]],
    courseObjectives: [null, [Validators.maxLength(255)]],
    courseSubTitle: [null, [Validators.required, Validators.maxLength(120)]],
    logo: [null, [Validators.required, Validators.maxLength(255)]],
    courseLevel: [],
    courseParentCategory: [],
    courseCategory: [],
  });

  constructor(
    protected courseService: InstructorCoursesService,
    protected courseLevelService: CourseLevelService,
    protected courseCategoryService: CourseCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    //this.handleNavigation();
    //this.loadAllCourses();
    this.loadRelationshipsOptions();
  }

  previousState(): void {
    window.history.back();
  }

  trackCourseLevelById(index: number, item: ICourseLevel): number {
    return item.id!;
  }

  trackCourseCategoryById(index: number, item: ICourseCategory): number {
    return item.id!;
  }

  save(data: any): void {
    delete data.courseParentCategory;
    this.courseService.create(data).subscribe(
      res => {
        window.alert('Created successfully');
        this.router.navigate(['instructor-courses']);
      },
      () => {
        window.alert('Error while creating course');
      }
    );
  }

  categorize(courseCategories: ICourseCategory[]): void {
    courseCategories.forEach(courseCategory => {
      this.courseCategories.push(courseCategory);
      if (courseCategory.isParent) {
        this.courseParentCategoriesSharedCollection.push(courseCategory);
      } else if (!courseCategory.isParent) {
        this.courseSubCategoriesSharedCollection.push(courseCategory);
      }
    });
  }

  onChange(): void {
    const parentId = this.editForm.get('courseParentCategory')?.value.id;
    this.courseSubCategoriesSharedCollection.length = 0;
    this.courseCategories.forEach(courseCategory => {
      if (courseCategory.parentId === parentId && !courseCategory.isParent) {
        this.courseSubCategoriesSharedCollection.push(courseCategory);
      }
    });
  }

  protected loadRelationshipsOptions(): void {
    this.courseLevelService
      .query()
      .pipe(map((res: HttpResponse<ICourseLevel[]>) => res.body ?? []))
      .pipe(
        map((courseLevels: ICourseLevel[]) =>
          this.courseLevelService.addCourseLevelToCollectionIfMissing(courseLevels, this.editForm.get('courseLevel')!.value)
        )
      )
      .subscribe((courseLevels: ICourseLevel[]) => (this.courseLevelsSharedCollection = courseLevels));

    this.courseCategoryService
      .query()
      .pipe(map((res: HttpResponse<ICourseCategory[]>) => res.body ?? []))
      .pipe(
        map((courseCategories: ICourseCategory[]) =>
          this.courseCategoryService.addCourseCategoryToCollectionIfMissing(courseCategories, this.editForm.get('courseCategory')!.value)
        )
      )
      .subscribe((courseCategories: ICourseCategory[]) => this.categorize(courseCategories));
  }
}
