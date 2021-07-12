import { Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseSectionDeleteDialogComponent } from 'app/entities/course-section/delete/course-section-delete-dialog.component';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { timeInterval, timeout } from 'rxjs/operators';

@Component({
  selector: 'jhi-user-course-sections',
  templateUrl: './user-course-sections.component.html',
  styleUrls: ['./user-course-sections.component.scss'],
})
export class UserCourseSectionsComponent implements OnInit {
  courseSections?: ICourseSection[] | null;
  isLoading = false;
  courseId!: string | null;
  sectionsSessions!: Map<ICourseSection, ICourseSession[]>;
  toggle = false;
  selectedSection: any;
  selectedSession: any;
  constructor(
    protected courseSectionService: InstructorCourseSectionService,
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.sectionsSessions = new Map();
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }
    this.loadSections();
  }

  trackId(index: number, item: ICourseSection): number {
    return item.id!;
  }

  delete(courseSection: ICourseSection): void {
    const modalRef = this.modalService.open(CourseSectionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courseSection = courseSection;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadSections();
      }
    });
  }
  toggleOne(data: any): void {
    this.selectedSection = data;
  }
  displayVideo(data: any): void {
    this.selectedSession = data;
    console.log(this.selectedSession);
  }
  loadPage(): void {
    this.isLoading = true;
    this.loadSections();
    this.isLoading = false;
  }

  private loadSections(): void {
    if (this.courseId !== null) {
      this.courseSectionService.query(this.courseId).subscribe(res => {
        this.courseSections = res.body;
      });
    }
    // console.log(this.courseSections?.length);
    this.courseSections?.forEach(section => {
      if (this.courseId && section.id) {
        const tempArr: ICourseSession[] = [];
        this.courseSessionService.query(this.courseId, section.id.toString()).subscribe(sessions => {
          if (!this.sectionsSessions.has(section) && sessions.body !== null) {
            this.sectionsSessions.set(section, sessions.body);
          }
        });
      }
    });

    // if(this.courseId!==null) {
    //   this.courseSectionService.getAllSectionsAndSessions(this.courseId).subscribe(res=>{
    //     console.log(res.body);
    //     this.sectionsSessions=res.body;
    //   });
    console.log(this.sectionsSessions);
    console.log(this.sectionsSessions.size);
    if (this.sectionsSessions.size > 0) {
      for (const entry of this.sectionsSessions.entries()) {
        this.selectedSection = entry[0];
        this.selectedSession = entry[1][0];
        break;
      }
    }
  }
}
