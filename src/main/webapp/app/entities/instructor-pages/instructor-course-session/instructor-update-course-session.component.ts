import { Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { ActivatedRoute } from '@angular/router';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { UploadFilesService } from 'app/entities/instructor-pages/services/upload-files.service';
import { HttpClient } from '@angular/common/http';

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
    sessionVideo: [''],
    sessionResource: [null, [Validators.maxLength(300)]],
    isPreview: [false, [Validators.required]],
    isDraft: [false, [Validators.required]],
    quizLink: [null, [Validators.maxLength(300)]],
  });
  loading = false;
  private courseId!: string | null;
  private courseSectionId!: string | null;
  private selectedFiles!: File;

  constructor(
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected uploadService: UploadFilesService,
    protected http: HttpClient
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
    if (target.files !== null && target.files[0].size < 1048576 * 4096) {
      this.selectedFiles = target.files[0];
      this.editForm.get('sessionVideo')?.setValue(target.files[0]);
    } else {
      this.editForm.get('sessionVideo')?.setValue('');
      window.alert('Please upload the file of size less than 4GB');
    }
  }

  async save(dt: any): Promise<void> {
    console.log(this.editForm.get('sessionVideo')?.value);
    if (this.editForm.get('sessionVideo')?.value !== '') {
      this.loading = true;
      if (this.courseSectionId !== null && this.courseId !== null) {
        const form_Data = new FormData();
        form_Data.append('file', this.editForm.get('sessionVideo')?.value);
        const data = await this.http.post('api/test', form_Data, { responseType: 'text' }).toPromise();
        //window.alert(data);
        // window.alert('after test call');
        //const file = this.selectedFiles;
        // const ans = await this.uploadService.uploadFile(file);
        //data.sessionVideo = file;
        //this.editForm.get('sessionVideo')?.setValue(data)
        const formData = new FormData();
        formData.append('id', this.editForm.get('id')?.value);
        formData.append('sessionTitle', this.editForm.get('sessionTitle')?.value);
        formData.append('sessionDescription', this.editForm.get('sessionDescription')?.value);
        formData.append('sessionVideo', data);
        formData.append('sessionResource', this.editForm.get('sessionResource')?.value);
        formData.append('isPreview', dt.isPreview);
        formData.append('isDraft', dt.isDraft);
        formData.append('quizLink', dt.quizLink);
        //
        //
        // window.alert(formData.get('sessionVideo'));
        // window.alert('calling create');
        this.courseSessionService.create(this.courseId, this.courseSectionId, formData).subscribe(
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
      this.loading = false;
    } else {
      window.alert('Please upload an appropriate video');
    }
  }
}
