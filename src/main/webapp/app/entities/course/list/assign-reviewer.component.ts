import { Component, OnInit } from '@angular/core';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseService } from 'app/entities/course/service/course.service';
import { ICourse } from 'app/entities/course/course.model';
import { AssignCategoryReviewerService } from 'app/entities/assign-category-reviewer/assign-category-reviewer.service';
import { IUser } from 'app/entities/user/user.model';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-assign-reviewer',
  templateUrl: './assign-reviewer.component.html',
})
export class AssignReviewerComponent implements OnInit {
  courseId!: string | null;
  categoryId!: string | null;
  course!: ICourse | null;
  reviewers: IUser[] = [];
  reviewerSelected: IUser | undefined;
  reviewerSelectedId!: string;

  constructor(
    protected courseService: CourseService,
    protected assignCategoryReviewerService: AssignCategoryReviewerService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) {}

  ngOnInit(): void {
    const hasCategoryId: boolean = this.activatedRoute.snapshot.paramMap.has('categoryId');
    if (hasCategoryId) {
      this.categoryId = this.activatedRoute.snapshot.paramMap.get('categoryId');
    }
    //fetching reviewers list
    if (this.categoryId !== null) {
      this.assignCategoryReviewerService.getReviewerByCategory(this.categoryId).subscribe(
        res => {
          this.reviewers = res.body;
          console.warn(this.reviewers);
        },
        err => {
          window.alert('We ran into some problem');
          console.warn(err);
        }
      );
    }

    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }
    if (this.courseId !== null) {
      this.courseService.find(this.courseId).subscribe(
        res => {
          this.course = res.body;
          //window.alert(this.categoryId)
        },
        err => {
          window.alert('Problem in fetching course.');
          console.warn(err);
        }
      );
    }
  }

  onChange(event: any): void {
    this.reviewerSelectedId = event;
    // for(const user of this.reviewers){
    //   if(user.id!.toString()===event){
    //     this.reviewerSelected = user
    //   }
    // }
    //this.reviewerSelected = event;
    //console.warn(this.reviewerSelected)
  }

  save(): void {
    //this.reviewerSelected = this.editForm.get(['reviewerSelected'])?.value
    if (this.courseId && this.reviewerSelectedId) {
      this.courseService.assignReviewerToCourse(this.courseId, this.reviewerSelectedId).subscribe(
        res => {
          window.alert('Reviewer assigned to course successfully');
        },
        err => {
          window.alert('Reviewer assignment failed');
        }
      );
    }
  }

  previousState(): void {
    window.history.back();
  }
}
