import { Routes } from '@angular/router';
import { AppLayoutComponent } from './layout/app-layout/app-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: AppLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'alerts',
        loadComponent: () =>
          import('./features/alerts/alerts.component').then(m => m.AlertsComponent)
      },
      {
        path: 'devices',
        loadComponent: () =>
          import('./features/devices/devices.component').then(m => m.DevicesComponent)
      },
      {
        path: 'trek-groups',
        loadComponent: () =>
          import('./features/trek-groups/trek-groups.component').then(m => m.TrekGroupsComponent)
      },
      {
        path: 'trekkers',
        loadComponent: () =>
          import('./features/trekkers/trekkers.component').then(m => m.TrekkersComponent)
      },
      {
        path: 'orders',
        loadComponent: () =>
          import('./features/orders/orders.component').then(m => m.OrdersComponent)
      },
      {
        path: 'device-assignments',
        loadComponent: () =>
          import('./features/device-assignments/device-assignments.component').then(m => m.DeviceAssignmentsComponent)
      }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
