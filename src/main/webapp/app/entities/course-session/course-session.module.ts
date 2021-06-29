import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseSessionComponent } from './list/course-session.component';
import { CourseSessionDetailComponent } from './detail/course-session-detail.component';
import { CourseSessionUpdateComponent } from './update/course-session-update.component';
import { CourseSessionDeleteDialogComponent } from './delete/course-session-delete-dialog.component';
import { CourseSessionRoutingModule } from './route/course-session-routing.module';

@NgModule({
  imports: [SharedModule, CourseSessionRoutingModule],
  declarations: [CourseSessionComponent, CourseSessionDetailComponent, CourseSessionUpdateComponent, CourseSessionDeleteDialogComponent],
  entryComponents: [CourseSessionDeleteDialogComponent],
})
export class CourseSessionModule {}
