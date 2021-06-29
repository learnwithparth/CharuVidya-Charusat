import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseProgressComponent } from './list/course-progress.component';
import { CourseProgressDetailComponent } from './detail/course-progress-detail.component';
import { CourseProgressUpdateComponent } from './update/course-progress-update.component';
import { CourseProgressDeleteDialogComponent } from './delete/course-progress-delete-dialog.component';
import { CourseProgressRoutingModule } from './route/course-progress-routing.module';

@NgModule({
  imports: [SharedModule, CourseProgressRoutingModule],
  declarations: [
    CourseProgressComponent,
    CourseProgressDetailComponent,
    CourseProgressUpdateComponent,
    CourseProgressDeleteDialogComponent,
  ],
  entryComponents: [CourseProgressDeleteDialogComponent],
})
export class CourseProgressModule {}
