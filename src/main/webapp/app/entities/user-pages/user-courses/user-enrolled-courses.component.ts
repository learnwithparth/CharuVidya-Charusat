import { Component, OnInit } from '@angular/core';
import { Course, ICourse } from 'app/entities/course/course.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserCourseService } from 'app/entities/user-pages/user-courses/user-courses.service';
import { HttpResponse } from '@angular/common/http';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';

@Component({
  selector: 'jhi-user-enrolled-courses',
  templateUrl: './user-enrolled-courses.component.html',
  styleUrls: ['./user-enrolled-courses.component.scss'],
})
export class UserEnrolledCoursesComponent implements OnInit {
  courses?: ICourse[] | null;
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

  private loadAllCourses(): void {
    this.userCourseService.getEnrolledCourses().subscribe(
      (res: HttpResponse<ICourse[]>) => {
        this.courses = res.body;
      },
      () => {
        window.alert('Error in fetching user enrolled courses');
      }
    );
  }
}
