import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';

@Component({
  templateUrl: './course-enrollment-delete-dialog.component.html',
})
export class CourseEnrollmentDeleteDialogComponent {
  courseEnrollment?: ICourseEnrollment;

  constructor(protected courseEnrollmentService: CourseEnrollmentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseEnrollmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
