import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseSession } from '../course-session.model';
import { CourseSessionService } from '../service/course-session.service';

@Component({
  templateUrl: './course-session-delete-dialog.component.html',
})
export class CourseSessionDeleteDialogComponent {
  courseSession?: ICourseSession;

  constructor(protected courseSessionService: CourseSessionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseSessionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
