import { Component, OnInit } from '@angular/core';
import { UserCourseProgressService } from 'app/entities/user-course-progress/service/user-course-progress.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IUserCourseProgress } from 'app/entities/user-course-progress/model/user-course-progress.model';

@Component({
  templateUrl: './user-course-progress-delete-dialog.component.html',
})
export class UserCourseProgressDeleteDialogComponent {
  userCourseProgress!: IUserCourseProgress;

  constructor(protected userCourseProgressService: UserCourseProgressService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userCourseProgressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
