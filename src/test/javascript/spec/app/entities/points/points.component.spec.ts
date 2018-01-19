/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { PointsTestModule } from '../../../test.module';
import { PointsComponent } from '../../../../../../main/webapp/app/entities/points/points.component';
import { PointsService } from '../../../../../../main/webapp/app/entities/points/points.service';
import { Points } from '../../../../../../main/webapp/app/entities/points/points.model';

describe('Component Tests', () => {

    describe('Points Management Component', () => {
        let comp: PointsComponent;
        let fixture: ComponentFixture<PointsComponent>;
        let service: PointsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PointsTestModule],
                declarations: [PointsComponent],
                providers: [
                    PointsService
                ]
            })
            .overrideTemplate(PointsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PointsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PointsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Points(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.points[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
