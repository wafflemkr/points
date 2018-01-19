import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PointsPointsModule } from './points/points.module';
import { PointsWeightModule } from './weight/weight.module';
import { PointsBloodPressureModule } from './blood-pressure/blood-pressure.module';
import { PointsPreferencesModule } from './preferences/preferences.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PointsPointsModule,
        PointsWeightModule,
        PointsBloodPressureModule,
        PointsPreferencesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PointsEntityModule {}
