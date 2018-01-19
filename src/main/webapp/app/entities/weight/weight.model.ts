import { BaseEntity } from './../../shared';

export class Weight implements BaseEntity {
    constructor(
        public id?: number,
        public timestamp?: any,
        public weight?: number,
        public userId?: number,
    ) {
    }
}
