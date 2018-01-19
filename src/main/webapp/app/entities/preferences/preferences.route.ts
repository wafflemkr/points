import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PreferencesComponent } from './preferences.component';
import { PreferencesDetailComponent } from './preferences-detail.component';
import { PreferencesPopupComponent } from './preferences-dialog.component';
import { PreferencesDeletePopupComponent } from './preferences-delete-dialog.component';

export const preferencesRoute: Routes = [
    {
        path: 'preferences',
        component: PreferencesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Preferences'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'preferences/:id',
        component: PreferencesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Preferences'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const preferencesPopupRoute: Routes = [
    {
        path: 'preferences-new',
        component: PreferencesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Preferences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'preferences/:id/edit',
        component: PreferencesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Preferences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'preferences/:id/delete',
        component: PreferencesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Preferences'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
