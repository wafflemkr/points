import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PointsSharedModule } from '../../shared';
import { PointsAdminModule } from '../../admin/admin.module';
import {
    PointsService,
    PointsPopupService,
    PointsComponent,
    PointsDetailComponent,
    PointsDialogComponent,
    PointsPopupComponent,
    PointsDeletePopupComponent,
    PointsDeleteDialogComponent,
    pointsRoute,
    pointsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pointsRoute,
    ...pointsPopupRoute,
];

@NgModule({
    imports: [
        PointsSharedModule,
        PointsAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PointsComponent,
        PointsDetailComponent,
        PointsDialogComponent,
        PointsDeleteDialogComponent,
        PointsPopupComponent,
        PointsDeletePopupComponent,
    ],
    entryComponents: [
        PointsComponent,
        PointsDialogComponent,
        PointsPopupComponent,
        PointsDeleteDialogComponent,
        PointsDeletePopupComponent,
    ],
    providers: [
        PointsService,
        PointsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PointsPointsModule {}
