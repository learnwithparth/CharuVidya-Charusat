import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseLevel } from '../course-level.model';
import { CourseLevelService } from '../service/course-level.service';

@Component({
  templateUrl: './course-level-delete-dialog.component.html',
})
export class CourseLevelDeleteDialogComponent {
  courseLevel?: ICourseLevel;

  constructor(protected courseLevelService: CourseLevelService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseLevelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
