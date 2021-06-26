import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICourseLevel, CourseLevel } from '../course-level.model';
import { CourseLevelService } from '../service/course-level.service';

@Component({
  selector: 'jhi-course-level-update',
  templateUrl: './course-level-update.component.html',
})
export class CourseLevelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    level: [null, [Validators.maxLength(20)]],
    description: [null, [Validators.maxLength(100)]],
  });

  constructor(protected courseLevelService: CourseLevelService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseLevel }) => {
      this.updateForm(courseLevel);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseLevel = this.createFromForm();
    if (courseLevel.id !== undefined) {
      this.subscribeToSaveResponse(this.courseLevelService.update(courseLevel));
    } else {
      this.subscribeToSaveResponse(this.courseLevelService.create(courseLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseLevel>>): void {
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

  protected updateForm(courseLevel: ICourseLevel): void {
    this.editForm.patchValue({
      id: courseLevel.id,
      level: courseLevel.level,
      description: courseLevel.description,
    });
  }

  protected createFromForm(): ICourseLevel {
    return {
      ...new CourseLevel(),
      id: this.editForm.get(['id'])!.value,
      level: this.editForm.get(['level'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
