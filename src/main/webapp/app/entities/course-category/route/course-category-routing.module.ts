import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseCategoryComponent } from '../list/course-category.component';
import { CourseCategoryDetailComponent } from '../detail/course-category-detail.component';
import { CourseCategoryUpdateComponent } from '../update/course-category-update.component';
import { CourseCategoryRoutingResolveService } from './course-category-routing-resolve.service';

const courseCategoryRoute: Routes = [
  {
    path: '',
    component: CourseCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseCategoryDetailComponent,
    resolve: {
      courseCategory: CourseCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseCategoryUpdateComponent,
    resolve: {
      courseCategory: CourseCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseCategoryUpdateComponent,
    resolve: {
      courseCategory: CourseCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courseCategoryRoute)],
  exports: [RouterModule],
})
export class CourseCategoryRoutingModule {}
