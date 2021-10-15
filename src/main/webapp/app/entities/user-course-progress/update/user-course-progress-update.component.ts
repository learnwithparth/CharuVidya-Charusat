import { Component, OnInit } from '@angular/core';
import { IUser } from 'app/entities/user/user.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseProgress, ICourseProgress } from 'app/entities/course-progress/course-progress.model';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { UserCourseProgressService } from 'app/entities/user-course-progress/service/user-course-progress.service';
import { UserService } from 'app/entities/user/user.service';
import { CourseService } from 'app/entities/course/service/course.service';
import { CourseProgressService } from 'app/entities/course-progress/service/course-progress.service';
import { IUserCourseProgress, UserCourseProgress } from 'app/entities/user-course-progress/model/user-course-progress.model';
import { finalize, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-user-course-progress-update',
  templateUrl: './user-course-progress-update.component.html',
  styleUrls: ['./user-course-progress-update.component.scss'],
})
export class UserCourseProgressUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  courses: ICourse[] = [];
  courseProgresses: ICourseProgress[] = [];
  editForm = this.fb.group({
    id: [null, [Validators.required]],
    user: [null, [Validators.required]],
    course: [null, [Validators.required]],
    courseProgress: [null, [Validators.required]],
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected userCourseProgressService: UserCourseProgressService,
    protected userService: UserService,
    protected courseService: CourseService,
    protected courseProgressService: CourseProgressService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userCourseProgress }) => {
      console.log(userCourseProgress);
      this.updateForm(userCourseProgress);
      this.loadRelationshipsOptions();
    });
  }
  previousState(): void {
    window.history.back();
  }
  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }
  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }
  trackCourseProgressById(index: number, item: IUserCourseProgress): number {
    return item.id!;
  }
  save(): void {
    this.isSaving = true;
    const userCourseProgress = this.createFromForm();
    if (userCourseProgress.id !== undefined) {
      this.subscribeToSaveResponse(this.userCourseProgressService.update(userCourseProgress));
    } else {
      this.subscribeToSaveResponse(this.userCourseProgressService.create(userCourseProgress));
    }
  }
  updateForm(userCourseProgress: IUserCourseProgress): void {
    this.editForm.patchValue({
      id: userCourseProgress.id === undefined ? null : userCourseProgress.id,
      user: userCourseProgress.user,
      course: userCourseProgress.course,
      courseProgress: userCourseProgress.courseProgress,
    });
    this.users = this.userService.addUserToCollectionIfMissing(this.users, userCourseProgress.user);
    this.courses = this.courseService.addCourseToCollectionIfMissing(this.courses, userCourseProgress.course);
    this.courseProgresses = this.courseProgressService.addCourseProgressToCollectionIfMissing(
      this.courseProgresses,
      userCourseProgress.courseProgress
    );
  }

  loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.users = users));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(courses, this.editForm.get('course')!.value)))
      .subscribe((courses: ICourse[]) => (this.courses = courses));

    this.courseProgressService
      .query()
      .pipe(map((res: HttpResponse<ICourseProgress[]>) => res.body ?? []))
      .pipe(map((cps: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(cps, this.editForm.get('courseProgress')!.value)))
      .subscribe((cps: ICourseProgress[]) => (this.courseProgresses = cps));
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserCourseProgress>>): void {
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

  protected createFromForm(): IUserCourseProgress {
    return {
      ...new UserCourseProgress(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      course: this.editForm.get(['course'])!.value,
      courseProgress: this.editForm.get(['courseProgress'])!.value,
    };
  }
}
