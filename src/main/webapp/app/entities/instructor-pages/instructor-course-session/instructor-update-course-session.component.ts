import { Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { ActivatedRoute } from '@angular/router';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { UploadFilesService } from 'app/entities/instructor-pages/services/upload-files.service';

@Component({
  selector: 'jhi-update-session',
  templateUrl: './instructor-update-course-session.component.html',
  styleUrls: ['./instructor-update-course-session.component.scss'],
})
export class InstructorUpdateCourseSessionComponent implements OnInit {
  isSaving = false;
  urlValue = '';
  videoPreview = false;
  editForm = this.fb.group({
    id: [null, [Validators.required]],
    sessionTitle: [null, [Validators.required, Validators.maxLength(255)]],
    sessionDescription: [null, [Validators.maxLength(255)]],
    sessionVideo: [null, [Validators.required, Validators.maxLength(300)]],
    sessionResource: [null, [Validators.maxLength(300)]],
    isPreview: [null, [Validators.required]],
    isDraft: [null, [Validators.required]],
  });
  loading = false;
  private courseId!: string | null;
  private courseSectionId!: string | null;
  private selectedFiles!: File;

  constructor(
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected uploadService: UploadFilesService
  ) {}

  ngOnInit(): void {
    this.loading = false;
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }

    const hasCourseSectionId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseSectionId) {
      this.courseSectionId = this.activatedRoute.snapshot.paramMap.get('courseSectionId');
    }
  }

  previousState(): void {
    window.history.back();
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
    } else if (url.includes('youtu.be/')) {
      const indexOfV = (url.indexOf('youtu.be/') as number) + 9;
      let indexOfEnd;
      if (url.includes('?')) {
        indexOfEnd = url.indexOf('?');
      } else {
        indexOfEnd = url.length;
      }
      this.urlValue = url.substring(indexOfV, indexOfEnd).trim();
      (this.editForm.get('sessionVideo') as FormControl).setValue(this.urlValue);
    } else {
      this.urlValue = '';
      (this.editForm.get('sessionVideo') as FormControl).setValue(null);
    }
  }

  selectFile(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files !== null) {
      this.selectedFiles = target.files[0];
    }
  }

  async save(data: any): Promise<void> {
    this.loading = true;
    if (this.courseSectionId !== null && this.courseId !== null) {
      const file = this.selectedFiles;
      const ans = await this.uploadService.uploadFile(file);
      data.sessionVideo = ans;
      this.courseSessionService.create(this.courseId, this.courseSectionId, data).subscribe(
        res => {
          this.loading = false;
          window.alert('Session added successfully');
          this.previousState();
        },
        () => {
          this.loading = false;
          window.alert('Error in adding session');
        }
      );
    }
  }
}
