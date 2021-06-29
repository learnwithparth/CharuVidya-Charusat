import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICourseCategory, CourseCategory } from '../course-category.model';
import { CourseCategoryService } from '../service/course-category.service';

@Component({
  selector: 'jhi-course-category-update',
  templateUrl: './course-category-update.component.html',
})
export class CourseCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    courseCategoryTitle: [null, [Validators.required, Validators.maxLength(255)]],
    logo: [null, [Validators.required, Validators.maxLength(255)]],
    isParent: [null, [Validators.required]],
    parentId: [null, [Validators.required]],
  });

  constructor(
    protected courseCategoryService: CourseCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseCategory }) => {
      this.updateForm(courseCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseCategory = this.createFromForm();
    if (courseCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.courseCategoryService.update(courseCategory));
    } else {
      this.subscribeToSaveResponse(this.courseCategoryService.create(courseCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseCategory>>): void {
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

  protected updateForm(courseCategory: ICourseCategory): void {
    this.editForm.patchValue({
      id: courseCategory.id,
      courseCategoryTitle: courseCategory.courseCategoryTitle,
      logo: courseCategory.logo,
      isParent: courseCategory.isParent,
      parentId: courseCategory.parentId,
    });
  }

  protected createFromForm(): ICourseCategory {
    return {
      ...new CourseCategory(),
      id: this.editForm.get(['id'])!.value,
      courseCategoryTitle: this.editForm.get(['courseCategoryTitle'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      isParent: this.editForm.get(['isParent'])!.value,
      parentId: this.editForm.get(['parentId'])!.value,
    };
  }
}
