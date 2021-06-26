import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourseEnrollment, CourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-course-enrollment-update',
  templateUrl: './course-enrollment-update.component.html',
})
export class CourseEnrollmentUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    enrollementDate: [null, [Validators.required]],
    lastAccessedDate: [null, [Validators.required]],
    user: [],
    course: [],
  });

  constructor(
    protected courseEnrollmentService: CourseEnrollmentService,
    protected userService: UserService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseEnrollment }) => {
      this.updateForm(courseEnrollment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseEnrollment = this.createFromForm();
    if (courseEnrollment.id !== undefined) {
      this.subscribeToSaveResponse(this.courseEnrollmentService.update(courseEnrollment));
    } else {
      this.subscribeToSaveResponse(this.courseEnrollmentService.create(courseEnrollment));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseEnrollment>>): void {
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

  protected updateForm(courseEnrollment: ICourseEnrollment): void {
    this.editForm.patchValue({
      id: courseEnrollment.id,
      enrollementDate: courseEnrollment.enrollementDate,
      lastAccessedDate: courseEnrollment.lastAccessedDate,
      user: courseEnrollment.user,
      course: courseEnrollment.course,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, courseEnrollment.user);
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(this.coursesSharedCollection, courseEnrollment.course);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(courses, this.editForm.get('course')!.value)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): ICourseEnrollment {
    return {
      ...new CourseEnrollment(),
      id: this.editForm.get(['id'])!.value,
      enrollementDate: this.editForm.get(['enrollementDate'])!.value,
      lastAccessedDate: this.editForm.get(['lastAccessedDate'])!.value,
      user: this.editForm.get(['user'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }
}
