import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseEnrollmentComponent } from './list/course-enrollment.component';
import { CourseEnrollmentDetailComponent } from './detail/course-enrollment-detail.component';
import { CourseEnrollmentUpdateComponent } from './update/course-enrollment-update.component';
import { CourseEnrollmentDeleteDialogComponent } from './delete/course-enrollment-delete-dialog.component';
import { CourseEnrollmentRoutingModule } from './route/course-enrollment-routing.module';

@NgModule({
  imports: [SharedModule, CourseEnrollmentRoutingModule],
  declarations: [
    CourseEnrollmentComponent,
    CourseEnrollmentDetailComponent,
    CourseEnrollmentUpdateComponent,
    CourseEnrollmentDeleteDialogComponent,
  ],
  entryComponents: [CourseEnrollmentDeleteDialogComponent],
})
export class CourseEnrollmentModule {}
