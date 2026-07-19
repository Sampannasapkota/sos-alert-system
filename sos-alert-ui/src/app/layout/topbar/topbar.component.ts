import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BadgeModule } from 'primeng/badge';
import { AlertService } from '../../core/services/alert.service';
import { Subscription, interval } from 'rxjs';
import { switchMap, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule, RouterModule, BadgeModule],
  template: `
    <header class="topbar">
      <div class="topbar-left">
        <span class="topbar-title">TrekShield</span>
        <span class="topbar-subtitle">SOS Alert Service</span>
      </div>
      <div class="topbar-right">
        <div class="alert-badge-wrapper" routerLink="/alerts" title="Active Alerts">
          <i class="pi pi-bell topbar-icon" [class.ringing]="criticalCount > 0"></i>
          <span *ngIf="criticalCount > 0" class="alert-count-badge">{{ criticalCount }}</span>
        </div>
        <div class="topbar-divider"></div>
        <div class="topbar-user">
          <div class="user-avatar">C</div>
          <span class="user-name">Coordinator</span>
        </div>
      </div>
    </header>
  `,
  styles: [`
    .topbar {
      height: 60px;
      background: var(--bg-surface);
      border-bottom: 1px solid var(--border-subtle);
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 1.75rem;
      flex-shrink: 0;
      position: relative;
      z-index: 50;
    }
    .topbar-left { display: flex; flex-direction: column; line-height: 1.2; }
    .topbar-title { font-weight: 700; font-size: 0.95rem; color: var(--color-text); }
    .topbar-subtitle { font-size: 0.72rem; color: var(--color-text-dim); letter-spacing: 0.03em; }
    .topbar-right { display: flex; align-items: center; gap: 1rem; }

    .alert-badge-wrapper {
      position: relative;
      width: 38px; height: 38px;
      display: flex; align-items: center; justify-content: center;
      border-radius: 10px;
      background: var(--bg-elevated);
      cursor: pointer;
      transition: background 0.2s;
    }
    .alert-badge-wrapper:hover { background: rgba(239,68,68,0.15); }
    .topbar-icon { font-size: 1.1rem; color: var(--color-text-dim); transition: color 0.2s; }
    .topbar-icon.ringing {
      color: var(--color-danger);
      animation: ring 0.8s ease-in-out infinite alternate;
    }
    @keyframes ring {
      0%  { transform: rotate(-12deg); }
      100%{ transform: rotate(12deg);  }
    }
    .alert-count-badge {
      position: absolute;
      top: -4px; right: -4px;
      background: var(--color-danger);
      color: #fff;
      border-radius: 50%;
      min-width: 18px; height: 18px;
      font-size: 10px; font-weight: 700;
      display: flex; align-items: center; justify-content: center;
      box-shadow: 0 0 8px rgba(239,68,68,0.6);
      animation: pulse-badge 1.5s ease-in-out infinite;
    }
    @keyframes pulse-badge {
      0%, 100% { transform: scale(1); }
      50% { transform: scale(1.15); }
    }
    .topbar-divider { width: 1px; height: 24px; background: var(--border-medium); }
    .topbar-user { display: flex; align-items: center; gap: 0.6rem; }
    .user-avatar {
      width: 32px; height: 32px;
      border-radius: 50%;
      background: linear-gradient(135deg, var(--color-info), #4338ca);
      display: flex; align-items: center; justify-content: center;
      font-weight: 700; font-size: 0.85rem; color: #fff;
    }
    .user-name { font-size: 0.85rem; font-weight: 500; color: var(--color-text-dim); }
  `]
})
export class TopbarComponent implements OnInit, OnDestroy {
  criticalCount = 0;
  private sub?: Subscription;

  constructor(private alertService: AlertService) { }

  ngOnInit(): void {
    this.sub = interval(30000).pipe(
      startWith(0),
      switchMap(() => this.alertService.getAll())
    ).subscribe(alerts => {
      this.criticalCount = alerts.filter(
        a => a.status === 'RECEIVED' || a.status === 'ESCALATED'
      ).length;
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }
}
