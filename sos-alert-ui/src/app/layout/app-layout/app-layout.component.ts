import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, ToastModule, ConfirmDialogModule, SidebarComponent, TopbarComponent],
  template: `
    <p-toast position="top-right"></p-toast>
    <p-confirmDialog
      [style]="{width:'420px'}"
      acceptButtonStyleClass="p-button-danger"
      rejectButtonStyleClass="p-button-text">
    </p-confirmDialog>

    <div class="layout-wrapper">
      <app-sidebar></app-sidebar>
      <div class="layout-main">
        <app-topbar></app-topbar>
        <div class="layout-content">
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .layout-wrapper {
      display: flex;
      height: 100vh;
      overflow: hidden;
    }
    .layout-main {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }
    .layout-content {
      flex: 1;
      overflow-y: auto;
      padding: 1.75rem 2rem;
      background: var(--bg-base);
    }
  `]
})
export class AppLayoutComponent {}
