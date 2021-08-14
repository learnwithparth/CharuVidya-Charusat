import { Component, OnInit } from '@angular/core';
import { ICourse } from 'app/entities/course/course.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { InstructorCoursesService } from 'app/entities/instructor-pages/instructor-courses/instructor-courses.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { CourseDeleteDialogComponent } from 'app/entities/course/delete/course-delete-dialog.component';
import { combineLatest } from 'rxjs';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons/faCheckCircle';
import { faClock } from '@fortawesome/free-solid-svg-icons/faClock';

@Component({
  selector: 'jhi-instructor-courses',
  templateUrl: './instructor-courses.component.html',
  styleUrls: ['./instructor-courses.component.scss'],
})
export class InstructorCoursesComponent implements OnInit {
  courses?: ICourse[] | null;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  faCheck = faCheckCircle;
  faPending = faClock;

  constructor(
    protected courseService: InstructorCoursesService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.loadAllCourses();
  }

  ngOnInit(): void {
    //this.handleNavigation();
    this.loadAllCourses();
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

  sendForApproval(courseId: number | undefined): void {
    if (courseId !== undefined) {
      this.courseService.approveCourse(courseId.toString()).subscribe(
        res => {
          window.alert('Your request is sent for reviewing. You will hear from us shortly.');
        },
        err => {
          window.alert('There is some problem procesing your request. Try again later.');
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

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ICourse[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/course'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.courses = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  private loadAllCourses(): void {
    this.courseService.getCourses().subscribe(
      (res: HttpResponse<ICourse[]>) => {
        this.courses = res.body;
      },
      () => {
        window.alert('error');
      }
    );
  }
}
