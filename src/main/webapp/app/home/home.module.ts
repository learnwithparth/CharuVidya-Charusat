import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { FlexModule } from '@angular/flex-layout';
import { CardsModule } from 'angular-bootstrap-md';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]), FlexModule, CardsModule, MatTooltipModule],
  declarations: [HomeComponent],
})
export class HomeModule {}
