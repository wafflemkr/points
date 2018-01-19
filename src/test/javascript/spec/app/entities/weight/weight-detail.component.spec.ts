/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { PointsTestModule } from '../../../test.module';
import { WeightDetailComponent } from '../../../../../../main/webapp/app/entities/weight/weight-detail.component';
import { WeightService } from '../../../../../../main/webapp/app/entities/weight/weight.service';
import { Weight } from '../../../../../../main/webapp/app/entities/weight/weight.model';

describe('Component Tests', () => {

    describe('Weight Management Detail Component', () => {
        let comp: WeightDetailComponent;
        let fixture: ComponentFixture<WeightDetailComponent>;
        let service: WeightService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PointsTestModule],
                declarations: [WeightDetailComponent],
                providers: [
                    WeightService
                ]
            })
            .overrideTemplate(WeightDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WeightDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WeightService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Weight(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.weight).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
