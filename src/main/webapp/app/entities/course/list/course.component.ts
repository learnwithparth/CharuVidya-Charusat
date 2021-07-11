import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourse } from '../course.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CourseService } from '../service/course.service';
import { CourseDeleteDialogComponent } from '../delete/course-delete-dialog.component';

@Component({
  selector: 'jhi-course',
  templateUrl: './course.component.html',
})
export class CourseComponent implements OnInit {
  courses?: ICourse[];
  coursesToBeDisplayed: ICourse[] = [];
  approvedCourses: ICourse[] = [];
  unApprovedCourses: ICourse[] = [];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.courseService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ICourse[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          this.bifurcate(res.body);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
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
      this.coursesToBeDisplayed = this.approvedCourses;
    } else if (filter === 'unApproved') {
      this.coursesToBeDisplayed = this.unApprovedCourses;
    } else if (filter === 'all') {
      this.coursesToBeDisplayed = this.courses!;
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
    this.coursesToBeDisplayed = this.courses;
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  private bifurcate(courses: ICourse[] | null): void {
    this.approvedCourses.length = 0;
    this.unApprovedCourses.length = 0;
    courses?.forEach(course => {
      if (course.isApproved) {
        this.approvedCourses.push(course);
      } else {
        this.unApprovedCourses.push(course);
      }
    });
  }
}
