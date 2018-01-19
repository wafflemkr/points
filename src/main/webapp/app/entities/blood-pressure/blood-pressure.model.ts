import { BaseEntity } from './../../shared';

export class BloodPressure implements BaseEntity {
    constructor(
        public id?: number,
        public timestamp?: any,
        public systolic?: number,
        public diastolic?: number,
        public userId?: number,
    ) {
    }
}
