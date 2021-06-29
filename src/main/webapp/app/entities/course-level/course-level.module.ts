import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseLevelComponent } from './list/course-level.component';
import { CourseLevelDetailComponent } from './detail/course-level-detail.component';
import { CourseLevelUpdateComponent } from './update/course-level-update.component';
import { CourseLevelDeleteDialogComponent } from './delete/course-level-delete-dialog.component';
import { CourseLevelRoutingModule } from './route/course-level-routing.module';

@NgModule({
  imports: [SharedModule, CourseLevelRoutingModule],
  declarations: [CourseLevelComponent, CourseLevelDetailComponent, CourseLevelUpdateComponent, CourseLevelDeleteDialogComponent],
  entryComponents: [CourseLevelDeleteDialogComponent],
})
export class CourseLevelModule {}
