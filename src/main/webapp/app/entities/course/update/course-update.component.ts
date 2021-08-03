import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourse, Course } from '../course.model';
import { CourseService } from '../service/course.service';
import { ICourseLevel } from 'app/entities/course-level/course-level.model';
import { CourseLevelService } from 'app/entities/course-level/service/course-level.service';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { CourseCategoryService } from 'app/entities/course-category/service/course-category.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { UploadFilesService } from 'app/entities/instructor-pages/services/upload-files.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;

  courseLevelsSharedCollection: ICourseLevel[] = [];
  courseCategoriesSharedCollection: ICourseCategory[] = [];
  usersSharedCollection: IUser[] = [];
  loading = false;
  editForm = this.fb.group({
    id: [null, [Validators.required]],
    courseTitle: [null, [Validators.required, Validators.maxLength(600)]],
    courseDescription: [null, [Validators.required, Validators.maxLength(255)]],
    courseObjectives: [null, [Validators.maxLength(255)]],
    courseSubTitle: [null, [Validators.required, Validators.maxLength(120)]],
    previewVideourl: [null, [Validators.required, Validators.maxLength(255)]],
    courseLength: [],
    logo: [null, [Validators.required, Validators.maxLength(255)]],
    courseCreatedOn: [null, [Validators.required]],
    courseUpdatedOn: [null, [Validators.required]],
    courseRootDir: [null, [Validators.maxLength(255)]],
    amount: [],
    isDraft: [null, [Validators.required]],
    isApproved: [null, [Validators.required]],
    courseApprovalDate: [],
    courseLevel: [],
    courseCategory: [],
    user: [],
    reviewer: [],
  });
  private selectedFiles!: File;
  private fileChange = false;

  constructor(
    protected courseService: CourseService,
    protected courseLevelService: CourseLevelService,
    protected courseCategoryService: CourseCategoryService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected uploadService: UploadFilesService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.updateForm(course);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  selectFile(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files !== null) {
      this.selectedFiles = target.files[0];
      this.fileChange = true;
    }
  }

  async save(): Promise<void> {
    this.isSaving = true;
    this.loading = true;
    if (this.fileChange) {
      const file = this.selectedFiles;
      const ans = await this.uploadService.uploadFile(file);
      this.editForm.get('logo')?.setValue(ans);
    }

    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
    this.loading = false;
  }

  trackCourseLevelById(index: number, item: ICourseLevel): number {
    return item.id!;
  }

  trackCourseCategoryById(index: number, item: ICourseCategory): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      courseTitle: course.courseTitle,
      courseDescription: course.courseDescription,
      courseObjectives: course.courseObjectives,
      courseSubTitle: course.courseSubTitle,
      previewVideourl: course.previewVideourl,
      courseLength: course.courseLength,
      logo: course.logo,
      courseCreatedOn: course.courseCreatedOn,
      courseUpdatedOn: course.courseUpdatedOn,
      courseRootDir: course.courseRootDir,
      amount: course.amount,
      isDraft: course.isDraft,
      isApproved: course.isApproved,
      courseApprovalDate: course.courseApprovalDate,
      courseLevel: course.courseLevel,
      courseCategory: course.courseCategory,
      user: course.user,
      reviewer: course.reviewer,
    });

    this.courseLevelsSharedCollection = this.courseLevelService.addCourseLevelToCollectionIfMissing(
      this.courseLevelsSharedCollection,
      course.courseLevel
    );
    this.courseCategoriesSharedCollection = this.courseCategoryService.addCourseCategoryToCollectionIfMissing(
      this.courseCategoriesSharedCollection,
      course.courseCategory
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, course.user, course.reviewer);
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
      .subscribe((courseCategories: ICourseCategory[]) => (this.courseCategoriesSharedCollection = courseCategories));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value, this.editForm.get('reviewer')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      courseTitle: this.editForm.get(['courseTitle'])!.value,
      courseDescription: this.editForm.get(['courseDescription'])!.value,
      courseObjectives: this.editForm.get(['courseObjectives'])!.value,
      courseSubTitle: this.editForm.get(['courseSubTitle'])!.value,
      previewVideourl: this.editForm.get(['previewVideourl'])!.value,
      courseLength: this.editForm.get(['courseLength'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      courseCreatedOn: this.editForm.get(['courseCreatedOn'])!.value,
      courseUpdatedOn: this.editForm.get(['courseUpdatedOn'])!.value,
      courseRootDir: this.editForm.get(['courseRootDir'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      isDraft: this.editForm.get(['isDraft'])!.value,
      isApproved: this.editForm.get(['isApproved'])!.value,
      courseApprovalDate: this.editForm.get(['courseApprovalDate'])!.value,
      courseLevel: this.editForm.get(['courseLevel'])!.value,
      courseCategory: this.editForm.get(['courseCategory'])!.value,
      user: this.editForm.get(['user'])!.value,
      reviewer: this.editForm.get(['reviewer'])!.value,
    };
  }
}
