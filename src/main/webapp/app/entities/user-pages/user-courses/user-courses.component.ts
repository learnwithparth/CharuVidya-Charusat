import { Component, OnInit } from '@angular/core';
import { Course, ICourse } from 'app/entities/course/course.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserCourseService } from 'app/entities/user-pages/user-courses/user-courses.service';
import { HttpResponse } from '@angular/common/http';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { faUserTie, faCalendarCheck, faEye, faUserFriends } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-user-courses',
  templateUrl: './user-courses.component.html',
  styleUrls: ['./user-courses.component.scss'],
})
export class UserCoursesComponent implements OnInit {
  faUserTie = faUserTie;
  faCalendarCheck = faCalendarCheck;
  faEye = faEye;
  faUserFriends = faUserFriends;

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

  onEnroll(courseId: number | undefined): void {
    if (courseId !== undefined) {
      this.userCourseService.onEnroll(courseId.toString()).subscribe(
        res => {
          window.alert('Enrolled Successful');
        },
        () => {
          window.alert('Error while enrolling in course');
        }
      );
    }
  }

  generateRandomNumber(min: number | undefined, max: number | undefined): number {
    if (min !== undefined && max !== undefined) {
      return Math.floor(Math.random() * (max - min + 1) + min);
    }
    return 0;
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
