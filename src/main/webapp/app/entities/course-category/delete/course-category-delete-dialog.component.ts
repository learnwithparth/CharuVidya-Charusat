import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseCategory } from '../course-category.model';
import { CourseCategoryService } from '../service/course-category.service';

@Component({
  templateUrl: './course-category-delete-dialog.component.html',
})
export class CourseCategoryDeleteDialogComponent {
  courseCategory?: ICourseCategory;

  constructor(protected courseCategoryService: CourseCategoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
