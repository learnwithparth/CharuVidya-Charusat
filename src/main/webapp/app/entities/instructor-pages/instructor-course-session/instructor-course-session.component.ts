import { Component, OnInit } from '@angular/core';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { CourseSessionDeleteDialogComponent } from 'app/entities/course-session/delete/course-session-delete-dialog.component';
import { combineLatest } from 'rxjs';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-instructor-course-session',
  templateUrl: './instructor-course-session.component.html',
  styleUrls: ['./instructor-course-session.component.scss'],
})
export class InstructorCourseSessionComponent implements OnInit {
  courseSessions?: ICourseSession[] | null;
  courseId!: string | null;
  courseSectionId!: string | null;
  isLoading = false;
  faCheck = faCheckCircle;

  constructor(
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }

    const hasCourseSectionId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseSectionId) {
      this.courseSectionId = this.activatedRoute.snapshot.paramMap.get('courseSectionId');
    }
    this.loadSessions();
  }

  trackId(index: number, item: ICourseSession): number {
    return item.id!;
  }

  delete(courseSession: ICourseSession): void {
    const modalRef = this.modalService.open(CourseSessionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courseSession = courseSession;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadSessions();
      }
    });
  }

  loadSessions(): void {
    if (this.courseId !== null && this.courseSectionId != null) {
      this.courseSessionService.query(this.courseId, this.courseSectionId).subscribe(res => {
        this.courseSessions = res.body;
      });
    }
  }

  loadPage(): void {
    this.isLoading = true;
    this.loadSessions();
    this.isLoading = false;
  }

  addSessions(): void {
    if (this.courseId !== null && this.courseSectionId !== null) {
      this.router.navigate([`course/${this.courseId}/section/${this.courseSectionId}/add-session`]);
    }
  }

  onClickBack(): void {
    if (this.courseId && this.courseSectionId) {
      this.router.navigate([`/course/${this.courseId}/sections`]);
    }
  }
}
