import { Component, OnInit } from '@angular/core';
import { ICourseProgress } from 'app/entities/course-progress/course-progress.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CourseProgressService } from 'app/entities/course-progress/service/course-progress.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { CourseProgressDeleteDialogComponent } from 'app/entities/course-progress/delete/course-progress-delete-dialog.component';
import { combineLatest } from 'rxjs';
import { IUserCourseProgress } from 'app/entities/user-course-progress/model/user-course-progress.model';
import { UserCourseProgressService } from 'app/entities/user-course-progress/service/user-course-progress.service';
import { UserCourseProgressDeleteDialogComponent } from 'app/entities/user-course-progress/delete/user-course-progress-delete-dialog.component';

@Component({
  selector: 'jhi-user-course-progress',
  templateUrl: './user-course-progress.component.html',
  styleUrls: ['./user-course-progress.component.scss'],
})
export class UserCourseProgressComponent implements OnInit {
  userCourseProgress?: IUserCourseProgress[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected userCourseProgressService: UserCourseProgressService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.userCourseProgressService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        // sort:["id"]
      })
      .subscribe(
        (res: HttpResponse<IUserCourseProgress[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
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

  trackId(index: number, item: IUserCourseProgress): number {
    return item.id!;
  }

  delete(userCourseProgress: IUserCourseProgress): void {
    const modalRef = this.modalService.open(UserCourseProgressDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userCourseProgress = userCourseProgress;

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
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

  protected onSuccess(data: IUserCourseProgress[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/user-course-progress'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.userCourseProgress = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
