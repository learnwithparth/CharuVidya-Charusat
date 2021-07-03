import { Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { FormBuilder, Validators } from '@angular/forms';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { ActivatedRoute } from '@angular/router';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';

@Component({
  selector: 'jhi-update-session',
  templateUrl: './instructor-update-course-session.component.html',
})
export class InstructorUpdateCourseSessionComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    sessionTitle: [null, [Validators.required, Validators.maxLength(255)]],
    sessionDescription: [null, [Validators.maxLength(255)]],
    sessionVideo: [null, [Validators.required, Validators.maxLength(300)]],
    sessionResource: [null, [Validators.maxLength(300)]],
    isPreview: [null, [Validators.required]],
    isDraft: [null, [Validators.required]],
  });
  private courseId!: string | null;
  private courseSectionId!: string | null;

  constructor(
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
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

  save(data: any): void {
    if (this.courseSectionId !== null && this.courseId !== null) {
      this.courseSessionService.create(this.courseId, this.courseSectionId, data).subscribe(
        res => {
          window.alert('Session added successfully');
          this.previousState();
        },
        () => {
          window.alert('Error in adding session');
        }
      );
    }
  }
}
