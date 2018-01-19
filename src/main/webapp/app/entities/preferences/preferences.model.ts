import { BaseEntity } from './../../shared';

export const enum WeightUnit {
    'LBS',
    'KG'
}

export class Preferences implements BaseEntity {
    constructor(
        public id?: number,
        public weeklyGoals?: number,
        public weightUnit?: WeightUnit,
        public userId?: number,
    ) {
    }
}
