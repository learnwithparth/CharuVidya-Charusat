import { Component, Input, OnInit } from '@angular/core';
import { IUser } from 'app/admin/user-management/user-management.model';
import { CourseCategory } from 'app/entities/course-category/course-category.model';
import { AssignCategoryReviewerService } from 'app/entities/assign-category-reviewer/assign-category-reviewer.service';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-assign-category-reviewer',
  templateUrl: './assign-category-reviewer.component.html',
  styleUrls: ['./assign-category-reviewer.component.scss'],
})
export class AssignCategoryReviewerComponent implements OnInit {
  instructors: IUser[] | undefined;
  courseCategory?: CourseCategory;

  editForm = this.fb.group({
    instructors: [],
  });

  constructor(
    protected assignCategoryReviewerService: AssignCategoryReviewerService,
    private fb: FormBuilder,
    public activeModal: NgbActiveModal,
    protected alertService: AlertService
  ) {}

  ngOnInit(): void {
    if (this.courseCategory) {
      this.assignCategoryReviewerService.getInstructors().subscribe(
        res => {
          this.instructors = res.body;
          this.editForm.patchValue({
            instructors: this.instructors,
          });
        },
        err => {
          console.error(err);
        }
      );
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  onSubmit(): void {
    const selectedReviewers = this.editForm.get(['instructors'])?.value;
    if (this.courseCategory) {
      this.assignCategoryReviewerService.setReviewersByCategory(selectedReviewers, this.courseCategory.id!).subscribe(
        res => {
          window.alert('Instructor(s) added as Reviewers(s)');
          this.activeModal.dismiss();
        },
        err => {
          console.error(err);
        }
      );
    }
  }
}
