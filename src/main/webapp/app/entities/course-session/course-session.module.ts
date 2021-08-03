import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseSessionComponent } from './list/course-session.component';
import { CourseSessionDetailComponent } from './detail/course-session-detail.component';
import { CourseSessionUpdateComponent } from './update/course-session-update.component';
import { CourseSessionDeleteDialogComponent } from './delete/course-session-delete-dialog.component';
import { CourseSessionRoutingModule } from './route/course-session-routing.module';
import { YouTubePlayerModule } from '@angular/youtube-player';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@NgModule({
  imports: [SharedModule, CourseSessionRoutingModule, YouTubePlayerModule, MatProgressBarModule],
  declarations: [CourseSessionComponent, CourseSessionDetailComponent, CourseSessionUpdateComponent, CourseSessionDeleteDialogComponent],
  entryComponents: [CourseSessionDeleteDialogComponent],
})
export class CourseSessionModule {}
