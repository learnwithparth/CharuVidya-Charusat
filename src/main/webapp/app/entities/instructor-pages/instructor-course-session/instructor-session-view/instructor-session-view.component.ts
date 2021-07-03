import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ICourseSession} from "app/entities/course-session/course-session.model";
import {InstructorCourseSessionService} from "app/entities/instructor-pages/instructor-course-session/instructor-course-session.service";

@Component({
  selector: 'jhi-instructor-session-view',
  templateUrl: './instructor-session-view.component.html',
  styleUrls: ['./instructor-session-view.component.scss']
})
export class InstructorSessionViewComponent implements OnInit {
  courseSession: ICourseSession | null = null;
  courseSessions?: ICourseSession[] | null;
  sessionId!: string | null ;
  courseSectionId!: string | null;
  isLoading = false;
  constructor(protected activatedRoute: ActivatedRoute,protected courseSessionService: InstructorCourseSessionService) {}

  ngOnInit(): void {
    this.sessionId="";
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('sessionId');
    if (hasCourseId) {
      this.sessionId = this.activatedRoute.snapshot.paramMap.get('sessionId');
    }

    this.loadSessions();
    this.activatedRoute.data.subscribe(({ courseSession }) => {
      this.courseSession = courseSession;
    });
  }

  previousState(): void {
    window.history.back();
  }
  loadSessions(): void {
    if ( this.sessionId!==null) {
      this.courseSessionService.find(this.sessionId).subscribe((data)=>{
          this.courseSession=data.body;
      })
    }
  }
}
