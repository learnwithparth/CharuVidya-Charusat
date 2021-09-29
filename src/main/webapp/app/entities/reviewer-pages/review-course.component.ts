import { Component, OnInit } from '@angular/core';
import { ICourse } from 'app/entities/course/course.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons/faCheckCircle';
import { CourseService } from 'app/entities/course/service/course.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CourseDeleteDialogComponent } from 'app/entities/course/delete/course-delete-dialog.component';

@Component({
  selector: 'jhi-review-course',
  templateUrl: './review-course.component.html',
})
export class ReviewCourseComponent implements OnInit {
  courses?: ICourse[];
  coursesToBeDisplayed: ICourse[] = [];
  approvedCourses: ICourse[] = [];
  unApprovedCourses: ICourse[] = [];
  approvalPendingCourses: ICourse[] = [];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  isCourseTypeApproval = false;
  faCheck = faCheckCircle;

  constructor(
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.isLoading = true;

    this.courseService.getCoursesForReview().subscribe(
      (res: HttpResponse<ICourse[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body);
        this.bifurcate(res.body);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  ngOnInit(): void {
    this.loadPage();
  }

  trackId(index: number, item: ICourse): number {
    return item.id!;
  }

  delete(course: ICourse): void {
    const modalRef = this.modalService.open(CourseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.course = course;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  approvedList(): void {
    this.coursesToBeDisplayed = this.approvedCourses;
  }

  unApprovedList(): void {
    this.coursesToBeDisplayed = this.unApprovedCourses;
  }

  allList(): void {
    this.coursesToBeDisplayed = this.courses!;
  }

  onFilter(event: any): void {
    const filter = event.target.value;
    if (filter === 'approved') {
      this.isCourseTypeApproval = false;
      this.coursesToBeDisplayed = this.approvedCourses;
    } else if (filter === 'unApproved') {
      this.isCourseTypeApproval = false;
      this.coursesToBeDisplayed = this.unApprovedCourses;
    } else if (filter === 'all') {
      this.isCourseTypeApproval = false;
      this.coursesToBeDisplayed = this.courses!;
    } else if (filter === 'approvalPending') {
      this.isCourseTypeApproval = true;
      this.coursesToBeDisplayed = this.approvalPendingCourses;
    }
  }

  approveCourse(courseId: number): void {
    if (courseId) {
      this.courseService.approveCourse(courseId).subscribe(
        res => {
          window.alert('Course approved successfully');
          this.loadPage();
        },
        error => {
          window.alert('Something went wrong');
        }
      );
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  // protected handleNavigation(): void {
  //   combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
  //     const page = params.get('page');
  //     const pageNumber = page !== null ? +page : 1;
  //     const sort = (params.get('sort') ?? data['defaultSort']).split(',');
  //     const predicate = sort[0];
  //     const ascending = sort[1] === 'asc';
  //     if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
  //       this.predicate = predicate;
  //       this.ascending = ascending;
  //       this.loadPage();
  //     }
  //   });
  // }

  protected onSuccess(data: ICourse[] | null): void {
    // this.totalItems = Number(headers.get('X-Total-Count'));
    // this.page = page;
    // if (navigate) {
    //   this.router.navigate(['/course'], {
    //     queryParams: {
    //       page: this.page,
    //       size: this.itemsPerPage,
    //       sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
    //     },
    //   });
    // }
    this.courses = data ?? [];
    this.coursesToBeDisplayed = this.courses;
    // this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  private bifurcate(courses: ICourse[] | null): void {
    this.approvedCourses.length = 0;
    this.unApprovedCourses.length = 0;
    courses?.forEach(course => {
      if (course.isApproved && !course.isDraft) {
        this.approvedCourses.push(course);
      } else if (!course.isApproved && course.isDraft) {
        this.unApprovedCourses.push(course);
      } else if (!course.isApproved && !course.isDraft) {
        this.approvalPendingCourses.push(course);
      }
    });
  }
}
