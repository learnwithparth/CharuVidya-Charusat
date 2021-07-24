import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICourseSession, CourseSession } from '../course-session.model';
import { CourseSessionService } from '../service/course-session.service';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { UploadFilesService } from 'app/entities/instructor-pages/services/upload-files.service';

@Component({
  selector: 'jhi-course-session-update',
  templateUrl: './course-session-update.component.html',
})
export class CourseSessionUpdateComponent implements OnInit {
  isSaving = false;
  urlValue = '';
  videoPreview = false;
  courseSectionsSharedCollection: ICourseSection[] = [];

  loading = false;
  editForm = this.fb.group({
    id: [null, [Validators.required]],
    sessionTitle: [null, [Validators.required, Validators.maxLength(255)]],
    sessionDescription: [null, [Validators.maxLength(255)]],
    sessionVideo: [null, [Validators.required, Validators.maxLength(300)]],
    sessionDuration: [null, [Validators.required]],
    sessionOrder: [null, [Validators.required]],
    sessionResource: [null, [Validators.maxLength(300)]],
    sessionLocation: [null, [Validators.required, Validators.maxLength(300)]],
    isPreview: [null, [Validators.required]],
    isDraft: [null, [Validators.required]],
    isApproved: [null, [Validators.required]],
    isPublished: [null, [Validators.required]],
    courseSection: [],
  });
  private selectedFiles!: File;
  private fileChange = false;

  constructor(
    protected courseSessionService: CourseSessionService,
    protected courseSectionService: CourseSectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected uploadService: UploadFilesService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseSession }) => {
      if (courseSession.id === undefined) {
        const today = dayjs().startOf('day');
        courseSession.sessionDuration = today;
      }

      this.updateForm(courseSession);

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
    this.loading = true;
    this.isSaving = true;
    if (this.fileChange) {
      const file = this.selectedFiles;
      const ans = await this.uploadService.uploadFile(file);
      this.editForm.get('sessionVideo')?.setValue(ans);
    }

    const courseSession = this.createFromForm();
    if (courseSession.id !== undefined) {
      this.subscribeToSaveResponse(this.courseSessionService.update(courseSession));
    } else {
      this.subscribeToSaveResponse(this.courseSessionService.create(courseSession));
    }
    this.loading = false;
  }

  trackCourseSectionById(index: number, item: ICourseSection): number {
    return item.id!;
  }
  videoUrlInput(val: any): void {
    const url = val.target.value;
    if (url.includes('v=')) {
      const indexOfV = (url.indexOf('v=') as number) + 2;
      let indexOfEnd;
      if (url.includes('&')) {
        indexOfEnd = url.indexOf('&');
      } else {
        indexOfEnd = url.length;
      }
      this.urlValue = url.substring(indexOfV, indexOfEnd).trim();
      (this.editForm.get('sessionVideo') as FormControl).setValue(this.urlValue);
    }
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseSession>>): void {
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

  protected updateForm(courseSession: ICourseSession): void {
    this.editForm.patchValue({
      id: courseSession.id,
      sessionTitle: courseSession.sessionTitle,
      sessionDescription: courseSession.sessionDescription,
      sessionVideo: courseSession.sessionVideo,
      sessionDuration: courseSession.sessionDuration ? courseSession.sessionDuration.format(DATE_TIME_FORMAT) : null,
      sessionOrder: courseSession.sessionOrder,
      sessionResource: courseSession.sessionResource,
      sessionLocation: courseSession.sessionLocation,
      isPreview: courseSession.isPreview,
      isDraft: courseSession.isDraft,
      isApproved: courseSession.isApproved,
      isPublished: courseSession.isPublished,
      courseSection: courseSession.courseSection,
    });

    this.courseSectionsSharedCollection = this.courseSectionService.addCourseSectionToCollectionIfMissing(
      this.courseSectionsSharedCollection,
      courseSession.courseSection
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseSectionService
      .query()
      .pipe(map((res: HttpResponse<ICourseSection[]>) => res.body ?? []))
      .pipe(
        map((courseSections: ICourseSection[]) =>
          this.courseSectionService.addCourseSectionToCollectionIfMissing(courseSections, this.editForm.get('courseSection')!.value)
        )
      )
      .subscribe((courseSections: ICourseSection[]) => (this.courseSectionsSharedCollection = courseSections));
  }

  protected createFromForm(): ICourseSession {
    return {
      ...new CourseSession(),
      id: this.editForm.get(['id'])!.value,
      sessionTitle: this.editForm.get(['sessionTitle'])!.value,
      sessionDescription: this.editForm.get(['sessionDescription'])!.value,
      sessionVideo: this.editForm.get(['sessionVideo'])!.value,
      sessionDuration: this.editForm.get(['sessionDuration'])!.value
        ? dayjs(this.editForm.get(['sessionDuration'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sessionOrder: this.editForm.get(['sessionOrder'])!.value,
      sessionResource: this.editForm.get(['sessionResource'])!.value,
      sessionLocation: this.editForm.get(['sessionLocation'])!.value,
      isPreview: this.editForm.get(['isPreview'])!.value,
      isDraft: this.editForm.get(['isDraft'])!.value,
      isApproved: this.editForm.get(['isApproved'])!.value,
      isPublished: this.editForm.get(['isPublished'])!.value,
      courseSection: this.editForm.get(['courseSection'])!.value,
    };
  }
}
