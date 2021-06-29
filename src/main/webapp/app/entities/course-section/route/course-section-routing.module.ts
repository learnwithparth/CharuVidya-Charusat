import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseSectionComponent } from '../list/course-section.component';
import { CourseSectionDetailComponent } from '../detail/course-section-detail.component';
import { CourseSectionUpdateComponent } from '../update/course-section-update.component';
import { CourseSectionRoutingResolveService } from './course-section-routing-resolve.service';

const courseSectionRoute: Routes = [
  {
    path: '',
    component: CourseSectionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseSectionDetailComponent,
    resolve: {
      courseSection: CourseSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseSectionUpdateComponent,
    resolve: {
      courseSection: CourseSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseSectionUpdateComponent,
    resolve: {
      courseSection: CourseSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseSectionRoute)],
  exports: [RouterModule],
})
export class CourseSectionRoutingModule {}
