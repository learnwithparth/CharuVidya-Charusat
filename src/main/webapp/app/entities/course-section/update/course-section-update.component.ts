import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourseSection, CourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-course-section-update',
  templateUrl: './course-section-update.component.html',
})
export class CourseSectionUpdateComponent implements OnInit {
  isSaving = false;

  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    sectionTitle: [null, [Validators.required, Validators.maxLength(255)]],
    sectionDescription: [null, [Validators.maxLength(255)]],
    sectionOrder: [null, [Validators.required]],
    isDraft: [null, [Validators.required]],
    isApproved: [null, [Validators.required]],
    course: [],
  });

  constructor(
    protected courseSectionService: CourseSectionService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseSection }) => {
      this.updateForm(courseSection);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseSection = this.createFromForm();
    if (courseSection.id !== undefined) {
      this.subscribeToSaveResponse(this.courseSectionService.update(courseSection));
    } else {
      this.subscribeToSaveResponse(this.courseSectionService.create(courseSection));
    }
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseSection>>): void {
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

  protected updateForm(courseSection: ICourseSection): void {
    this.editForm.patchValue({
      id: courseSection.id,
      sectionTitle: courseSection.sectionTitle,
      sectionDescription: courseSection.sectionDescription,
      sectionOrder: courseSection.sectionOrder,
      isDraft: courseSection.isDraft,
      isApproved: courseSection.isApproved,
      course: courseSection.course,
    });

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(this.coursesSharedCollection, courseSection.course);
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(courses, this.editForm.get('course')!.value)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): ICourseSection {
    return {
      ...new CourseSection(),
      id: this.editForm.get(['id'])!.value,
      sectionTitle: this.editForm.get(['sectionTitle'])!.value,
      sectionDescription: this.editForm.get(['sectionDescription'])!.value,
      sectionOrder: this.editForm.get(['sectionOrder'])!.value,
      isDraft: this.editForm.get(['isDraft'])!.value,
      isApproved: this.editForm.get(['isApproved'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }
}
