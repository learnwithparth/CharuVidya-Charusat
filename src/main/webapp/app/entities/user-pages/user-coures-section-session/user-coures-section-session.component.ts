import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseSectionDeleteDialogComponent } from 'app/entities/course-section/delete/course-section-delete-dialog.component';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { CourseSessionDeleteDialogComponent } from 'app/entities/course-session/delete/course-session-delete-dialog.component';

@Component({
  selector: 'jhi-user-coures-section-session',
  templateUrl: './user-coures-section-session.component.html',
  styleUrls: ['./user-coures-section-session.component.scss'],
})
export class UserCouresSectionSessionComponent implements OnInit {
  courseSessions?: ICourseSession[] | null;
  courseId!: string | null;
  courseSectionId!: string | null;
  isLoading = false;

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
    window.history.back();
  }
}
