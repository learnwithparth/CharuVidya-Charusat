import { Component, OnInit } from '@angular/core';
import { Course, ICourse } from 'app/entities/course/course.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserCourseService } from 'app/entities/user-pages/user-courses/user-courses.service';
import { HttpResponse } from '@angular/common/http';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';

@Component({
  selector: 'jhi-user-courses',
  templateUrl: './user-courses.component.html',
  styleUrls: ['./user-courses.component.scss'],
})
export class UserCoursesComponent implements OnInit {
  courses?: ICourse[] | null;
  categoryId!: string | null;
  studentCount: Map<ICourse, number> = new Map<ICourse, number>();

  constructor(
    protected userCourseService: UserCourseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAllCourses();
  }

  onEnroll(course: ICourse): void {
    this.userCourseService.onEnroll(course).subscribe(
      res => {
        window.alert('Enrolled Successful');
      },
      () => {
        window.alert('Error while enrolling in course');
      }
    );
  }

  private loadAllCourses(): void {
    const hasCategoryId: boolean = this.activatedRoute.snapshot.paramMap.has('categoryId');
    if (hasCategoryId) {
      this.categoryId = this.activatedRoute.snapshot.paramMap.get('categoryId');
    }
    if (this.categoryId !== null) {
      this.userCourseService.query(this.categoryId).subscribe(
        (res: HttpResponse<ICourseCategory[]>) => {
          this.courses = res.body;
          this.courses?.forEach(course => {
            if (course.id != null) {
              this.userCourseService.getStudentCount(course.id).subscribe(count => {
                this.studentCount.set(course, count.body);
              });
            }
          });
        },
        () => {
          window.alert('Error in fetching parent categories');
        }
      );
    }
  }
}
