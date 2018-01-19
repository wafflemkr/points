import { BaseEntity } from './../../shared';

export class Points implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public exercise?: number,
        public meals?: number,
        public alcohol?: number,
        public notes?: string,
        public userId?: number,
    ) {
    }
}
