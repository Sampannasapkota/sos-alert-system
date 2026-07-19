import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterLinkActive } from '@angular/router';
import { RippleModule } from 'primeng/ripple';

interface NavItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, RippleModule],
  template: `
    <nav class="sidebar">
      <div class="sidebar-brand">
        <div class="brand-icon">
          <i class="pi pi-shield"></i>
        </div>
        <div class="brand-text">
          <span class="brand-name">TrekShield</span>
          <span class="brand-sub">SOS Alert Service</span>
        </div>
      </div>

      <div class="sidebar-section-label">OPERATIONS</div>

      <ul class="nav-list">
        <li *ngFor="let item of navItems">
          <a
            class="nav-item"
            [routerLink]="item.route"
            routerLinkActive="active"
            pRipple>
            <i [class]="'nav-icon pi ' + item.icon"></i>
            <span class="nav-label">{{ item.label }}</span>
          </a>
        </li>
      </ul>
    </nav>
  `,
  styles: [`
    .sidebar {
      width: var(--sidebar-width);
      min-width: var(--sidebar-width);
      background: var(--bg-surface);
      border-right: 1px solid var(--border-subtle);
      display: flex;
      flex-direction: column;
      height: 100vh;
      position: relative;
      z-index: 100;
    }

    .sidebar-brand {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 1.25rem 1.25rem 1rem;
      border-bottom: 1px solid var(--border-subtle);
    }
    .brand-icon {
      width: 38px; height: 38px;
      background: linear-gradient(135deg, var(--color-danger), #b91c1c);
      border-radius: 10px;
      display: flex; align-items: center; justify-content: center;
      font-size: 1.1rem;
      color: white;
      flex-shrink: 0;
      box-shadow: 0 0 14px rgba(239,68,68,0.4);
    }
    .brand-text { display: flex; flex-direction: column; line-height: 1.1; }
    .brand-name { font-weight: 800; font-size: 1rem; color: var(--color-text); letter-spacing: 0.05em; }
    .brand-sub  { font-size: 0.65rem; color: var(--color-text-dim); text-transform: uppercase; letter-spacing: 0.1em; }

    .sidebar-section-label {
      padding: 1.25rem 1.25rem 0.4rem;
      font-size: 10px;
      font-weight: 700;
      letter-spacing: 0.12em;
      color: var(--color-muted);
      text-transform: uppercase;
    }

    .nav-list { list-style: none; margin: 0; padding: 0 0.75rem; }

    .nav-item {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 0.65rem 0.75rem;
      border-radius: 8px;
      color: var(--color-text-dim);
      text-decoration: none;
      font-size: 0.875rem;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.18s;
      margin-bottom: 2px;
      position: relative;
      overflow: hidden;
    }
    .nav-item:hover {
      color: var(--color-text);
      background: var(--bg-elevated);
    }
    .nav-item.active {
      color: #fff;
      background: linear-gradient(90deg, rgba(99,102,241,0.2), rgba(99,102,241,0.05));
      border-left: 2px solid var(--color-info);
    }
    .nav-item.active .nav-icon { color: var(--color-info); }

    .nav-icon { font-size: 1rem; width: 20px; flex-shrink: 0; }
    .nav-label { flex: 1; }

    .sidebar-footer {
      margin-top: auto;
      padding: 1rem 1.25rem;
      border-top: 1px solid var(--border-subtle);
    }
    .footer-status {
      display: flex; align-items: center; gap: 0.5rem;
      font-size: 0.75rem; color: var(--color-text-dim);
    }
    .status-dot {
      width: 7px; height: 7px;
      border-radius: 50%;
      background: var(--color-success);
      box-shadow: 0 0 6px var(--color-success);
      animation: blink 2s ease-in-out infinite;
    }
    @keyframes blink {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.3; }
    }
  `]
})
export class SidebarComponent {
  navItems: NavItem[] = [
    { label: 'Dashboard', icon: 'pi-home', route: '/dashboard' },
    { label: 'SOS Alerts', icon: 'pi-bell', route: '/alerts' },
    { label: 'Devices', icon: 'pi-mobile', route: '/devices' },
    { label: 'Trek Groups', icon: 'pi-users', route: '/trek-groups' },
    { label: 'Trekkers', icon: 'pi-user', route: '/trekkers' },
    { label: 'Orders', icon: 'pi-file', route: '/orders' },
    { label: 'Device Assignments', icon: 'pi-link', route: '/device-assignments' },
  ];
}
