import { NgModule, LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import * as dayjs from 'dayjs';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

import { SERVER_API_URL } from './app.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { AppRoutingModule } from './app-routing.module';
import { HomeModule } from './home/home.module';
import { EntityRoutingModule } from './entities/entity-routing.module';
import { YouTubePlayerModule } from '@angular/youtube-player';
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';

// jhipster-needle-angular-add-module-import JHipster will add new module here
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { UserCourseCategoryComponent } from './entities/user-pages/user-course-category/user-course-category.component';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { FlexLayoutModule } from '@angular/flex-layout';
import { UserCourseSubCategoriesComponent } from './entities/user-pages/user-course-sub-categories/user-course-sub-categories.component';
import { UserCoursesComponent } from './entities/user-pages/user-courses/user-courses.component';
import { InstructorCoursesComponent } from './entities/instructor-pages/instructor-courses/instructor-courses.component';
import { InstructorUpdateCoursesComponent } from 'app/entities/instructor-pages/instructor-courses/instructor-update-courses.component';
import { CardsModule } from 'angular-bootstrap-md';
import { InstructorCoursesectionComponent } from './entities/instructor-pages/instructor-coursesection/instructor-coursesection.component';
import { InstructorUpdateCoursesectionComponent } from 'app/entities/instructor-pages/instructor-coursesection/instructor-update-coursesection.component';
import { InstructorCourseSessionComponent } from './entities/instructor-pages/instructor-course-session/instructor-course-session.component';
import { InstructorUpdateCourseSessionComponent } from 'app/entities/instructor-pages/instructor-course-session/instructor-update-course-session.component';
import { InstructorSessionViewComponent } from './entities/instructor-pages/instructor-course-session/instructor-session-view/instructor-session-view.component';
import { UserCourseSessionComponent } from 'app/entities/user-pages/user-course-session/user-course-session.component';
import { UserCourseSectionsComponent } from 'app/entities/user-pages/user-course-sections/user-course-sections.component';
import { UserCouresSectionSessionComponent } from './entities/user-pages/user-coures-section-session/user-coures-section-session.component';

@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    EntityRoutingModule,
    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-' }),
    MatCardModule,
    MatToolbarModule,
    MatButtonModule,
    FlexLayoutModule,
    CardsModule,
    YouTubePlayerModule,
    VgCoreModule,
    VgControlsModule,
    VgOverlayPlayModule,
    VgBufferingModule,
  ],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    httpInterceptorProviders,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    FooterComponent,
    UserCourseCategoryComponent,
    UserCourseSubCategoriesComponent,
    UserCoursesComponent,
    UserCourseSectionsComponent,
    UserCourseSessionComponent,
    InstructorCoursesComponent,
    InstructorUpdateCoursesComponent,
    InstructorCoursesectionComponent,
    InstructorUpdateCoursesectionComponent,
    InstructorCourseSessionComponent,
    InstructorUpdateCourseSessionComponent,
    InstructorSessionViewComponent,
    UserCouresSectionSessionComponent,
  ],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
