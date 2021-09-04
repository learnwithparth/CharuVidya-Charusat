import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ICourseSession } from 'app/entities/course-session/course-session.model';
import { InstructorCourseSessionService } from 'app/entities/instructor-pages/instructor-course-session/instructor-course-session.service';

@Component({
  selector: 'jhi-instructor-session-view',
  templateUrl: './instructor-session-view.component.html',
  styleUrls: ['./instructor-session-view.component.scss'],
})
export class InstructorSessionViewComponent implements OnInit, AfterViewInit {
  courseSession: ICourseSession | null = null;
  courseSessions?: ICourseSession[] | null;
  sessionId!: string | null;
  courseSectionId!: string | null;
  isLoading = false;
  video: any = null;
  constructor(protected activatedRoute: ActivatedRoute, protected courseSessionService: InstructorCourseSessionService) {}

  ngOnInit(): void {
    this.sessionId = '';
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('sessionId');
    if (hasCourseId) {
      this.sessionId = this.activatedRoute.snapshot.paramMap.get('sessionId');
    }

    this.loadSessions();
    this.activatedRoute.data.subscribe(({ courseSession }) => {
      this.courseSession = courseSession;
    });

    //
  }
  ngAfterViewInit(): void {
    console.log('@');
    this.video = document.getElementById('singleVideo');
    console.log(this.video);
    console.log(this.video.currentTime);
    setInterval(() => {
      if (!this.video.paused && !this.video.ended) {
        console.log(this.video.currentTime);
      } else if (this.video.ended) {
        console.log(this.video.currentTime);
      }
    }, 5000);
  }
  previousState(): void {
    window.history.back();
  }
  loadSessions(): void {
    if (this.sessionId !== null) {
      this.courseSessionService.find(this.sessionId).subscribe(data => {
        this.courseSession = data.body;
      });
    }
  }

  approve(courseSession: ICourseSession | null): void {
    if (courseSession?.id) {
      this.courseSessionService.approveSession(courseSession).subscribe(
        res => {
          window.alert('Session approved');
        },
        error => {
          console.error(error);
          window.alert('Something went wrong');
        }
      );
    }
  }
}
