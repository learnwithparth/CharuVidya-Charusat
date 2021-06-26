import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseProgressComponent } from '../list/course-progress.component';
import { CourseProgressDetailComponent } from '../detail/course-progress-detail.component';
import { CourseProgressUpdateComponent } from '../update/course-progress-update.component';
import { CourseProgressRoutingResolveService } from './course-progress-routing-resolve.service';

const courseProgressRoute: Routes = [
  {
    path: '',
    component: CourseProgressComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseProgressDetailComponent,
    resolve: {
      courseProgress: CourseProgressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseProgressUpdateComponent,
    resolve: {
      courseProgress: CourseProgressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseProgressUpdateComponent,
    resolve: {
      courseProgress: CourseProgressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseProgressRoute)],
  exports: [RouterModule],
})
export class CourseProgressRoutingModule {}
