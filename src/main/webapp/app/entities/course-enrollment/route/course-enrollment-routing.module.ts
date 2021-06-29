import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseEnrollmentComponent } from '../list/course-enrollment.component';
import { CourseEnrollmentDetailComponent } from '../detail/course-enrollment-detail.component';
import { CourseEnrollmentUpdateComponent } from '../update/course-enrollment-update.component';
import { CourseEnrollmentRoutingResolveService } from './course-enrollment-routing-resolve.service';

const courseEnrollmentRoute: Routes = [
  {
    path: '',
    component: CourseEnrollmentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseEnrollmentDetailComponent,
    resolve: {
      courseEnrollment: CourseEnrollmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseEnrollmentUpdateComponent,
    resolve: {
      courseEnrollment: CourseEnrollmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseEnrollmentUpdateComponent,
    resolve: {
      courseEnrollment: CourseEnrollmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseEnrollmentRoute)],
  exports: [RouterModule],
})
export class CourseEnrollmentRoutingModule {}
