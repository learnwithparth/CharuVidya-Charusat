import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourseCategoryComponent } from './list/course-category.component';
import { CourseCategoryDetailComponent } from './detail/course-category-detail.component';
import { CourseCategoryUpdateComponent } from './update/course-category-update.component';
import { CourseCategoryDeleteDialogComponent } from './delete/course-category-delete-dialog.component';
import { CourseCategoryRoutingModule } from './route/course-category-routing.module';

@NgModule({
  imports: [SharedModule, CourseCategoryRoutingModule],
  declarations: [
    CourseCategoryComponent,
    CourseCategoryDetailComponent,
    CourseCategoryUpdateComponent,
    CourseCategoryDeleteDialogComponent,
  ],
  entryComponents: [CourseCategoryDeleteDialogComponent],
})
export class CourseCategoryModule {}
