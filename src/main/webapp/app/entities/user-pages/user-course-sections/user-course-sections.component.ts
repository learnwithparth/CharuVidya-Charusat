import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseSectionDeleteDialogComponent } from 'app/entities/course-section/delete/course-section-delete-dialog.component';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';
import { ICourseSession } from 'app/entities/course-session/course-session.model';

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
  allSessions: any = [];
  sectionIndex = 0;
  video: any = null;
  constructor(
    protected courseSectionService: InstructorCourseSectionService,
    protected courseSessionService: InstructorCourseSessionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.sectionsSessions = new Map<ICourseSection, ICourseSession[]>();
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }
    this.loadSections();
  }
  ngAfterViewInit(): void {
    setInterval(() => {
      const v = document.getElementById('singleVideo');
      if (v !== null) {
        console.log(v.getAttribute('currentTime'));
      }
    }, 5000);
  }

  trackId(index: number, item: ICourseSection): number {
    return item.id!;
  }
  onClickBack(): void {
    window.history.back();
  }
  // playVideo(): void {
  //   this.api.play();
  // }
  // nextVideo(): void {
  //   // this.selectedSession = this.allSessions[this.sectionIndex][1];
  // }
  // onPlayerReady(api: any): void {
  //   this.api = api;
  //   console.log(api);
  //   this.api.getDefaultMedia().subscriptions.loadedMetadata.subscribe(this.playVideo.bind(this));
  //   this.api.getDefaultMedia().subscriptions.ended.subscribe(this.nextVideo.bind(this));
  // }

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
  toggleOne(sections: any, index: number): void {
    this.selectedSection = sections;
    this.sectionIndex = index;
  }
  displayVideo(data: any): void {
    this.selectedSession = data;
    // this.url = this.selectedSection.sessionVideo;
    const video = document.getElementById('singleVideo');
    console.log(video);
    if (video !== null) {
      video.setAttribute('src', this.selectedSession.sessionVideo);
    }
  }
  loadPage(): void {
    this.isLoading = true;
    this.loadSections();
    this.isLoading = false;
  }

  // private loadSections(): void {
  //   if (this.courseId !== null) {
  //     this.courseSectionService.query(this.courseId).subscribe(res => {
  //       this.courseSections = res.body;
  //     });
  //   }
  //   // console.log(this.courseSections?.length);
  //   this.courseSections?.forEach(section => {
  //     if (this.courseId && section.id) {
  //       const tempArr: ICourseSession[] = [];
  //       this.courseSessionService.query(this.courseId, section.id.toString()).subscribe(sessions => {
  //         if (!this.sectionsSessions.has(section) && sessions.body !== null) {
  //           this.sectionsSessions.set(section, sessions.body);
  //         }
  //       });
  //     }
  //   });
  //
  //   // if(this.courseId!==null) {
  //   //   this.courseSectionService.getAllSectionsAndSessions(this.courseId).subscribe(res=>{
  //   //     console.log(res.body);
  //   //     this.sectionsSessions=res.body;
  //   //   });
  //   console.log(this.sectionsSessions);
  //   console.log(this.sectionsSessions.size);
  //   if (this.sectionsSessions.size > 0) {
  //     for (const entry of this.sectionsSessions.entries()) {
  //       this.selectedSection = entry[0];
  //       this.selectedSession = entry[1][0];
  //       break;
  //     }
  //   }
  //   this.url=this.selectedSession.sessionVideo;
  // }
  private async loadSections(): Promise<void> {
    if (this.courseId !== null) {
      this.courseSectionService.query(this.courseId).subscribe(res => {
        this.courseSections = res.body;
        // console.log(res.body);
      });
      // console.log(this.courseSections);
      const res = await this.courseSectionService.getAllSectionsAndSessions(this.courseId).toPromise();
      this.sectionsSessions = res.body;
      console.log(this.sectionsSessions);
      // console.log('#', Object.keys(this.sectionsSessions).length);
      // for(const entry of this.sectionsSessions.entries()){
      //   this.selectedSection = entry[0];
      //   this.selectedSession = entry[1][0];
      //   break;
      // }
      // for(const [key,value] of Object.entries(this.sectionsSessions)){
      //     this.selectedSection = key;
      //     this.selectedSession = value[0];
      //     break;

      if (this.courseSections) {
        this.selectedSection = this.courseSections[0];
      }
      for (const value of Object.values(this.sectionsSessions)) {
        this.allSessions.push(value);
      }
      this.selectedSession = this.allSessions[0][0];
      // this.url = this.selectedSession.sessionVideo;
      // console.log(this.selectedSection);
      // console.log(this.selectedSection);
      // console.log(this.allSessions);
    }
  }
}
