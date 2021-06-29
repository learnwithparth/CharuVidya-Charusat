import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseSessionComponent } from '../list/course-session.component';
import { CourseSessionDetailComponent } from '../detail/course-session-detail.component';
import { CourseSessionUpdateComponent } from '../update/course-session-update.component';
import { CourseSessionRoutingResolveService } from './course-session-routing-resolve.service';

const courseSessionRoute: Routes = [
  {
    path: '',
    component: CourseSessionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseSessionDetailComponent,
    resolve: {
      courseSession: CourseSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseSessionUpdateComponent,
    resolve: {
      courseSession: CourseSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseSessionUpdateComponent,
    resolve: {
      courseSession: CourseSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseSessionRoute)],
  exports: [RouterModule],
})
export class CourseSessionRoutingModule {}
