import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';

@Component({
  templateUrl: './course-section-delete-dialog.component.html',
})
export class CourseSectionDeleteDialogComponent {
  courseSection?: ICourseSection;

  constructor(protected courseSectionService: CourseSectionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseSectionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
