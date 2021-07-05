import { Component, OnInit } from '@angular/core';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseSessionDeleteDialogComponent } from 'app/entities/course-session/delete/course-session-delete-dialog.component';

@Component({
  selector: 'jhi-user-course-session',
  templateUrl: './user-course-session.component.html',
  styleUrls: ['./user-course-session.component.scss'],
})
export class UserCourseSessionComponent implements OnInit {
  courseSessions?: ICourseSession[] | null;
  courseId!: string | null;
  courseSectionId!: string | null;
  isLoading = false;
  urlValue: string | undefined;
  selectedSessionId!: number | undefined;
  selectedSession: any;
  constructor(
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.urlValue = '';
  }

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
        if (this.courseSessions) {
          this.urlValue = this.courseSessions[0].sessionVideo;
          this.selectedSessionId = this.courseSessions[0].id;
          this.selectedSession = this.courseSessions[0];
        }
      });
    }
  }

  loadPage(): void {
    this.isLoading = true;
    this.loadSessions();
    this.isLoading = false;
  }

  changeVideo(session: any): void {
    if (this.selectedSession.id !== session.id) {
      this.urlValue = session.sessionVideo;
      this.selectedSessionId = session.id;
      this.selectedSession = session;
    }
  }
}
