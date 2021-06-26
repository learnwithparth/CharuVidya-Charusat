import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseLevelComponent } from '../list/course-level.component';
import { CourseLevelDetailComponent } from '../detail/course-level-detail.component';
import { CourseLevelUpdateComponent } from '../update/course-level-update.component';
import { CourseLevelRoutingResolveService } from './course-level-routing-resolve.service';

const courseLevelRoute: Routes = [
  {
    path: '',
    component: CourseLevelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseLevelDetailComponent,
    resolve: {
      courseLevel: CourseLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseLevelUpdateComponent,
    resolve: {
      courseLevel: CourseLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseLevelUpdateComponent,
    resolve: {
      courseLevel: CourseLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseLevelRoute)],
  exports: [RouterModule],
})
export class CourseLevelRoutingModule {}
