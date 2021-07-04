import { Component, OnInit } from '@angular/core';
import { ICourse } from 'app/entities/course/course.model';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';

@Component({
  selector: 'jhi-update-section',
  templateUrl: './instructor-update-coursesection.component.html',
})
export class InstructorUpdateCoursesectionComponent implements OnInit {
  isSaving = false;
  courseId!: string | null;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    sectionTitle: [null, [Validators.required, Validators.maxLength(255)]],
    sectionDescription: [null, [Validators.maxLength(255)]],
  });

  constructor(
    protected courseSectionService: InstructorCourseSectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const hasCourseId = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(data: any): void {
    if (this.courseId != null) {
      this.courseSectionService.create(this.courseId, data).subscribe(
        res => {
          window.alert('Created successfully');
          this.previousState();
        },
        () => {
          window.alert('Error while adding session');
        }
      );
    }
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }
}
