import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseSectionComponent } from './list/course-section.component';
import { CourseSectionDetailComponent } from './detail/course-section-detail.component';
import { CourseSectionUpdateComponent } from './update/course-section-update.component';
import { CourseSectionDeleteDialogComponent } from './delete/course-section-delete-dialog.component';
import { CourseSectionRoutingModule } from './route/course-section-routing.module';

@NgModule({
  imports: [SharedModule, CourseSectionRoutingModule],
  declarations: [CourseSectionComponent, CourseSectionDetailComponent, CourseSectionUpdateComponent, CourseSectionDeleteDialogComponent],
  entryComponents: [CourseSectionDeleteDialogComponent],
})
export class CourseSectionModule {}
