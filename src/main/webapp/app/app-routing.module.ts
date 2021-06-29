import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserCourseCategoryComponent } from 'app/entities/user-pages/user-course-category/user-course-category.component';
import { UserCourseSubCategoriesComponent } from 'app/entities/user-pages/user-course-sub-categories/user-course-sub-categories.component';
import { UserCoursesComponent } from 'app/entities/user-pages/user-courses/user-courses.component';
import {A} from "@angular/cdk/keycodes";

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN, Authority.FACULTY, Authority.STUDENT, Authority.REVIEWER],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },
        {
          path: 'categories',
          data: {
            authorities: [Authority.STUDENT],
          },
          canActivate: [UserRouteAccessService],
          component: UserCourseCategoryComponent,
        },
        {
          path: 'sub-category/:parentId',
          data: {
            authorities: [Authority.STUDENT],
          },
          canActivate: [UserRouteAccessService],
          component: UserCourseSubCategoriesComponent,
        },
        {
          path: 'courses/:categoryId',
          data: {
            authorities: [Authority.STUDENT],
          },
          canActivate: [UserRouteAccessService],
          component: UserCoursesComponent,
        },
        ...LAYOUT_ROUTES,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
