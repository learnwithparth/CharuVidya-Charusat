import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseProgress } from '../course-progress.model';
import { CourseProgressService } from '../service/course-progress.service';

@Component({
  templateUrl: './course-progress-delete-dialog.component.html',
})
export class CourseProgressDeleteDialogComponent {
  courseProgress?: ICourseProgress;

  constructor(protected courseProgressService: CourseProgressService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseProgressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
