import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseSectionDeleteDialogComponent } from 'app/entities/course-section/delete/course-section-delete-dialog.component';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { CourseProgressService } from 'app/entities/course-progress/service/course-progress.service';
import { CourseProgress, ICourseProgress } from 'app/entities/course-progress/course-progress.model';
import { faPlayCircle } from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'jhi-user-course-sections',
  templateUrl: './user-course-sections.component.html',
  styleUrls: ['./user-course-sections.component.scss'],
})
export class UserCourseSectionsComponent implements OnInit, AfterViewInit {
  courseSections?: ICourseSection[] | null;
  isLoading = false;
  courseId!: string | null;
  sectionsSessions!: Map<ICourseSection, ICourseSession[]>;
  toggle = false;
  selectedSection: any;
  selectedSession: any;
  allSessions: any[][] = [];
  allSession1: any = [];
  sectionIndex = 0;
  sessionIndex = 0;
  video: any = null;
  courseProgress!: ICourseProgress;
  faPlayCircle = faPlayCircle;
  constructor(
    protected courseSectionService: InstructorCourseSectionService,
    protected courseSessionService: InstructorCourseSessionService,
    protected courseProgressService: CourseProgressService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.sectionsSessions = new Map<ICourseSection, ICourseSession[]>();
    this.courseProgress = new CourseProgress(undefined, false, 0, null, null);
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }
    this.loadSections();
  }
  ngAfterViewInit(): void {
    this.courseProgressService.findBySessionId(1).subscribe(
      res => {
        console.log(res.body);
        const temp: any = document.getElementById('singleVideo');
        if (temp != null) {
          console.log(temp);
          temp.currentTime = res.body?.watchSeconds;
        }
      },
      err => {
        console.log(err);
      }
    );
    setInterval(() => {
      const v = document.getElementById('singleVideo');
      if (v !== null) {
        this.video = v;
        // console.log(v);
        // console.log(this.video.currentTime);
        this.courseProgress.courseSession = this.selectedSession;
        this.courseProgress.watchSeconds = this.video.currentTime;
        // this.courseProgressService.updateUserWatchTime(new CourseProgress(undefined,false,this.video.currentTime,null,this.selectedSession));
        this.courseProgressService.updateUserWatchTime(this.courseProgress);
      }
    }, 150000000);
  }

  trackId(index: number, item: ICourseSection): number {
    return item.id!;
  }
  onClickBack(): void {
    window.history.back();
  }
  toggleSection(sections: any, index: number): void {
    this.selectedSection = sections;
    this.sectionIndex = index;
  }
  displayVideo(data: any, index: number): void {
    this.selectedSession = data;
    this.sessionIndex = index;
    // this.url = this.selectedSection.sessionVideo;
    const video = document.getElementById('singleVideo');
    console.log(video);
    if (video !== null) {
      video.setAttribute('src', this.selectedSession.sessionVideo);
    }
    this.updateVideoTime();
  }
  playNext(): void {
    // this.sessionIndex++;
    // if (this.allSessions[this.sectionIndex].length === this.sessionIndex && this.allSessions.length === this.sectionIndex + 1) {
    //   alert('Congrats🎉🎉 \nYou have completed the course.');
    //   return;
    // }
    // if (this.allSessions[this.sectionIndex].length === this.sessionIndex) {
    //   this.sessionIndex = 0;
    //   this.sectionIndex++;
    //   if (this.courseSections) {
    //     this.selectedSection = this.courseSections[this.sectionIndex];
    //   }
    // }
    // this.selectedSession = this.allSessions[this.sectionIndex][this.sessionIndex];
    this.sessionIndex++;
    if (this.allSession1.length === this.sessionIndex) {
      alert('Congrats🎉🎉 \nYou have completed the course.');
      return;
    }
    this.selectedSession = this.allSession1[this.sessionIndex];
    this.updateVideoTime();
  }
  loadPage(): void {
    this.isLoading = true;
    this.loadSections();
    this.isLoading = false;
  }
  updateVideoTime(): void {
    this.courseProgressService.findBySessionId(this.selectedSession.id).subscribe(
      res => {
        console.log(res.body);
        const temp: any = document.getElementById('singleVideo');
        if (temp != null) {
          console.log(temp);
          temp.currentTime = res.body?.watchSeconds as number;
        }
      },
      err => {
        console.log(err);
      }
    );
  }
  private async loadSections(): Promise<void> {
    if (this.courseId !== null) {
      this.courseSectionService.query(this.courseId).subscribe(res => {
        this.courseSections = res.body;
        // console.log(res.body);
      });

      const res = await this.courseSectionService.getAllSectionsAndSessions(this.courseId).toPromise();
      this.sectionsSessions = res.body;

      if (this.courseSections) {
        this.selectedSection = this.courseSections[0];
      }
      for (const value of Object.values(this.sectionsSessions)) {
        this.allSessions.push(value);
        this.allSession1 = this.allSession1.concat(value);
      }
      this.selectedSession = this.allSessions[0][0];
    }
  }
}
