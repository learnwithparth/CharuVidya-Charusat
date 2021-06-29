import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course-level',
        data: { pageTitle: 'CourseLevels' },
        loadChildren: () => import('./course-level/course-level.module').then(m => m.CourseLevelModule),
      },
      {
        path: 'course-category',
        data: { pageTitle: 'CourseCategories' },
        loadChildren: () => import('./course-category/course-category.module').then(m => m.CourseCategoryModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'Courses' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'course-enrollment',
        data: { pageTitle: 'CourseEnrollments' },
        loadChildren: () => import('./course-enrollment/course-enrollment.module').then(m => m.CourseEnrollmentModule),
      },
      {
        path: 'course-section',
        data: { pageTitle: 'CourseSections' },
        loadChildren: () => import('./course-section/course-section.module').then(m => m.CourseSectionModule),
      },
      {
        path: 'course-session',
        data: { pageTitle: 'CourseSessions' },
        loadChildren: () => import('./course-session/course-session.module').then(m => m.CourseSessionModule),
      },
      {
        path: 'course-progress',
        data: { pageTitle: 'CourseProgresses' },
        loadChildren: () => import('./course-progress/course-progress.module').then(m => m.CourseProgressModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
