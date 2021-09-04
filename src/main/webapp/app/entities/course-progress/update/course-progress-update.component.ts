import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICourseProgress, CourseProgress } from '../course-progress.model';
import { CourseProgressService } from '../service/course-progress.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { CourseSessionService } from 'app/entities/course-session/service/course-session.service';

@Component({
  selector: 'jhi-course-progress-update',
  templateUrl: './course-progress-update.component.html',
})
export class CourseProgressUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  courseSessionsSharedCollection: ICourseSession[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    completed: [null, [Validators.required]],
    watchSeconds: [null, [Validators.required]],
    user: [],
    courseSession: [],
  });

  constructor(
    protected courseProgressService: CourseProgressService,
    protected userService: UserService,
    protected courseSessionService: CourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseProgress }) => {
      if (courseProgress.id === undefined) {
        const today = dayjs().startOf('day');
        courseProgress.watchSeconds = today;
      }

      this.updateForm(courseProgress);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseProgress = this.createFromForm();
    if (courseProgress.id !== undefined) {
      this.subscribeToSaveResponse(this.courseProgressService.update(courseProgress));
    } else {
      this.subscribeToSaveResponse(this.courseProgressService.create(courseProgress));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCourseSessionById(index: number, item: ICourseSession): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseProgress>>): void {
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

  protected updateForm(courseProgress: ICourseProgress): void {
    this.editForm.patchValue({
      id: courseProgress.id,
      completed: courseProgress.completed,
      watchSeconds: courseProgress.watchSeconds,
      user: courseProgress.user,
      courseSession: courseProgress.courseSession,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, courseProgress.user);
    this.courseSessionsSharedCollection = this.courseSessionService.addCourseSessionToCollectionIfMissing(
      this.courseSessionsSharedCollection,
      courseProgress.courseSession
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.courseSessionService
      .query()
      .pipe(map((res: HttpResponse<ICourseSession[]>) => res.body ?? []))
      .pipe(
        map((courseSessions: ICourseSession[]) =>
          this.courseSessionService.addCourseSessionToCollectionIfMissing(courseSessions, this.editForm.get('courseSession')!.value)
        )
      )
      .subscribe((courseSessions: ICourseSession[]) => (this.courseSessionsSharedCollection = courseSessions));
  }

  protected createFromForm(): ICourseProgress {
    return {
      ...new CourseProgress(),
      id: this.editForm.get(['id'])!.value,
      completed: this.editForm.get(['completed'])!.value,
      watchSeconds: this.editForm.get(['watchSeconds'])!.value,
      user: this.editForm.get(['user'])!.value,
      courseSession: this.editForm.get(['courseSession'])!.value,
    };
  }
}
