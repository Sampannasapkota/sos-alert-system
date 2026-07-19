import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ChartModule } from 'primeng/chart';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { AlertService } from '../../core/services/alert.service';
import { Alert, AlertStatus } from '../../core/models/models';
import { Subscription, interval } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ChartModule, TagModule, ButtonModule, ProgressSpinnerModule],
  template: `
    <div class="page-header">
      <div style="display:flex; align-items:center; justify-content:space-between;">
        <div>
          <h1>📡 Operations Dashboard</h1>
          <p>Real-time SOS alert overview for active trekking expeditions</p>
        </div>
        <div style="display:flex; align-items:center; gap:0.75rem;">
          <span *ngIf="loading" class="refresh-label">
            <p-progressSpinner [style]="{width:'18px',height:'18px'}" strokeWidth="4"></p-progressSpinner>
            Refreshing...
          </span>
          <span class="last-refreshed">Last refresh: {{ lastRefreshed | date:'HH:mm:ss' }}</span>
        </div>
      </div>
    </div>

    <!-- Stat Cards -->
    <div class="stat-grid">
      <div class="card stat-card stat-danger" *ngIf="stats.received > 0 || true">
        <div class="stat-icon danger"><i class="pi pi-exclamation-triangle"></i></div>
        <div class="stat-info">
          <div class="stat-value" [class.pulse-number]="stats.received > 0">{{ stats.received }}</div>
          <div class="stat-label">RECEIVED</div>
          <div class="stat-sub">Awaiting response</div>
        </div>
      </div>
      <div class="card stat-card stat-warn">
        <div class="stat-icon warn"><i class="pi pi-arrow-up-right"></i></div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.escalated }}</div>
          <div class="stat-label">ESCALATED</div>
          <div class="stat-sub">Auto-escalated</div>
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-icon info"><i class="pi pi-user"></i></div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.claimed }}</div>
          <div class="stat-label">CLAIMED</div>
          <div class="stat-sub">Being handled</div>
        </div>
      </div>
      <div class="card stat-card stat-success">
        <div class="stat-icon success"><i class="pi pi-check-circle"></i></div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.resolved }}</div>
          <div class="stat-label">RESOLVED</div>
          <div class="stat-sub">Completed</div>
        </div>
      </div>
      <div class="card stat-card stat-info">
        <div class="stat-icon total"><i class="pi pi-bell"></i></div>
        <div class="stat-info">
          <div class="stat-value">{{ alerts.length }}</div>
          <div class="stat-label">TOTAL</div>
          <div class="stat-sub">All time alerts</div>
        </div>
      </div>
    </div>

    <div class="dashboard-grid">
      <!-- Recent Critical Alerts -->
      <div class="card recent-alerts-card">
        <div class="card-header">
          <h3>🚨 Active Alerts</h3>
          <a routerLink="/alerts" class="view-all-link">View all →</a>
        </div>
        <div *ngIf="criticalAlerts.length === 0" class="empty-state">
          <i class="pi pi-check-circle" style="font-size:2rem; color:var(--color-success)"></i>
          <p>All clear! No active alerts.</p>
        </div>
        <div *ngFor="let alert of criticalAlerts" class="alert-row" [ngClass]="'alert-row-' + alert.status.toLowerCase()">
          <div class="alert-row-left">
            <div class="alert-status-dot" [ngClass]="'dot-' + alert.status.toLowerCase()"></div>
            <div>
              <div class="alert-device">{{ alert.deviceCode }}</div>
              <div class="alert-trek">{{ alert.trekName }} · {{ alert.trekGroupName }}</div>
            </div>
          </div>
          <div class="alert-row-right">
            <span class="alert-status-badge" [ngClass]="'badge-' + alert.status.toLowerCase()">{{ alert.status }}</span>
            <div class="alert-time">{{ alert.alertTimestamp | date:'MMM d, HH:mm' }}</div>
          </div>
        </div>
      </div>

      <!-- Donut Chart -->
      <div class="card chart-card">
        <div class="card-header">
          <h3>📊 Alert Distribution</h3>
        </div>
        <div *ngIf="alerts.length === 0" class="empty-state">
          <i class="pi pi-chart-pie" style="font-size:2rem; color:var(--color-muted)"></i>
          <p>No data yet</p>
        </div>
        <p-chart *ngIf="alerts.length > 0"
          type="doughnut"
          [data]="chartData"
          [options]="chartOptions"
          [style]="{height:'260px'}">
        </p-chart>
        <div class="chart-legend">
          <div *ngFor="let item of legendItems" class="legend-item">
            <span class="legend-dot" [style.background]="item.color"></span>
            <span>{{ item.label }}</span>
            <span class="legend-count">{{ item.count }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Alert Lifecycle Info -->
    <div class="card lifecycle-card">
      <div class="card-header"><h3>⚡ Alert Lifecycle</h3></div>
      <div class="lifecycle-flow">
        <div class="lifecycle-step">
          <div class="lifecycle-icon received"><i class="pi pi-bell"></i></div>
          <div class="lifecycle-label">RECEIVED</div>
        </div>
        <div class="lifecycle-arrow"><i class="pi pi-arrow-right"></i></div>
        <div class="lifecycle-step">
          <div class="lifecycle-icon claimed"><i class="pi pi-user"></i></div>
          <div class="lifecycle-label">CLAIMED</div>
        </div>
        <div class="lifecycle-arrow"><i class="pi pi-arrow-right"></i></div>
        <div class="lifecycle-step">
          <div class="lifecycle-icon escalated"><i class="pi pi-arrow-up-right"></i></div>
          <div class="lifecycle-label">ESCALATED</div>
        </div>
        <div class="lifecycle-arrow"><i class="pi pi-arrow-right"></i></div>
        <div class="lifecycle-step">
          <div class="lifecycle-icon resolved"><i class="pi pi-check-circle"></i></div>
          <div class="lifecycle-label">RESOLVED</div>
        </div>
      </div>
      <div class="lifecycle-note">
        Unclaimed alerts are automatically escalated after the configured threshold.
        Duplicate alerts from the same device within 2 minutes are suppressed.
      </div>
    </div>
  `,
  styles: [`
    .stat-grid {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 1rem;
      margin-bottom: 1.5rem;
    }
    @media(max-width:1200px) { .stat-grid { grid-template-columns: repeat(3,1fr); } }
    @media(max-width:768px)  { .stat-grid { grid-template-columns: repeat(2,1fr); } }

    .stat-card {
      display: flex;
      align-items: center;
      gap: 1rem;
      padding: 1.25rem !important;
      transition: transform 0.2s;
    }
    .stat-card:hover { transform: translateY(-2px); }

    .stat-icon {
      width: 48px; height: 48px;
      border-radius: 12px;
      display: flex; align-items: center; justify-content: center;
      font-size: 1.3rem;
      flex-shrink: 0;
    }
    .stat-icon.danger  { background: rgba(239,68,68,0.15);  color: var(--color-danger); }
    .stat-icon.warn    { background: rgba(245,158,11,0.15); color: var(--color-warn); }
    .stat-icon.info    { background: rgba(99,102,241,0.15); color: var(--color-info); }
    .stat-icon.success { background: rgba(16,185,129,0.15); color: var(--color-success); }
    .stat-icon.total   { background: rgba(148,163,184,0.1); color: var(--color-muted); }

    .stat-value {
      font-size: 2rem;
      font-weight: 800;
      line-height: 1;
      color: var(--color-text);
    }
    .stat-label { font-size: 11px; font-weight: 700; letter-spacing: 0.08em; color: var(--color-text-dim); margin-top: 2px; }
    .stat-sub   { font-size: 11px; color: var(--color-muted); margin-top: 1px; }

    @keyframes pulse-number {
      0%, 100% { color: var(--color-text); }
      50% { color: var(--color-danger); }
    }
    .pulse-number { animation: pulse-number 1.5s ease-in-out infinite; }

    .dashboard-grid {
      display: grid;
      grid-template-columns: 1fr 380px;
      gap: 1rem;
      margin-bottom: 1rem;
    }
    @media(max-width:1024px) { .dashboard-grid { grid-template-columns: 1fr; } }

    .card-header {
      display: flex; align-items: center; justify-content: space-between;
      margin-bottom: 1.25rem;
      h3 { margin: 0; font-size: 1rem; font-weight: 700; }
    }
    .view-all-link {
      font-size: 0.8rem; color: var(--color-info); text-decoration: none;
      font-weight: 600;
    }
    .view-all-link:hover { text-decoration: underline; }

    .empty-state {
      text-align: center; padding: 2.5rem 1rem;
      color: var(--color-text-dim);
      i { display: block; margin-bottom: 0.75rem; }
      p { margin: 0; font-size: 0.9rem; }
    }

    .alert-row {
      display: flex; align-items: center; justify-content: space-between;
      padding: 0.75rem 0.85rem;
      border-radius: 8px;
      margin-bottom: 0.5rem;
      border: 1px solid var(--border-subtle);
      background: var(--bg-elevated);
      transition: all 0.2s;
    }
    .alert-row:hover { border-color: var(--border-medium); }
    .alert-row-received  { border-left: 3px solid var(--color-danger); animation: pulse-row 2.5s ease-in-out infinite; }
    .alert-row-escalated { border-left: 3px solid var(--color-warn); }
    .alert-row-claimed   { border-left: 3px solid var(--color-info); }

    .alert-row-left  { display: flex; align-items: center; gap: 0.75rem; }
    .alert-row-right { display: flex; flex-direction: column; align-items: flex-end; gap: 0.25rem; }

    .alert-status-dot {
      width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0;
    }
    .dot-received  { background: var(--color-danger); box-shadow: 0 0 6px var(--color-danger); animation: blink 1s infinite; }
    .dot-escalated { background: var(--color-warn);   box-shadow: 0 0 6px var(--color-warn); }
    .dot-claimed   { background: var(--color-info);   box-shadow: 0 0 6px var(--color-info); }
    @keyframes blink { 0%,100% { opacity:1; } 50% { opacity:0.2; } }

    .alert-device  { font-weight: 600; font-size: 0.875rem; }
    .alert-trek    { font-size: 0.75rem; color: var(--color-text-dim); margin-top: 1px; }
    .alert-time    { font-size: 0.7rem; color: var(--color-muted); }

    .alert-status-badge {
      font-size: 10px; font-weight: 700; letter-spacing: 0.06em;
      padding: 2px 8px; border-radius: 4px;
    }
    .badge-received  { background: rgba(239,68,68,0.15); color: var(--color-danger); }
    .badge-escalated { background: rgba(245,158,11,0.15); color: var(--color-warn); }
    .badge-claimed   { background: rgba(99,102,241,0.15); color: var(--color-info); }
    .badge-resolved  { background: rgba(16,185,129,0.15); color: var(--color-success); }

    .chart-legend {
      display: flex; flex-wrap: wrap; gap: 0.75rem; margin-top: 1rem;
      justify-content: center;
    }
    .legend-item {
      display: flex; align-items: center; gap: 0.4rem;
      font-size: 0.78rem; color: var(--color-text-dim);
    }
    .legend-dot { width: 10px; height: 10px; border-radius: 50%; }
    .legend-count { font-weight: 700; color: var(--color-text); margin-left: 2px; }

    .lifecycle-card { margin-top: 0; }
    .lifecycle-flow {
      display: flex; align-items: center; justify-content: center;
      gap: 0.75rem; flex-wrap: wrap;
      padding: 1rem 0;
    }
    .lifecycle-step { display: flex; flex-direction: column; align-items: center; gap: 0.5rem; }
    .lifecycle-icon {
      width: 52px; height: 52px; border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      font-size: 1.3rem;
    }
    .lifecycle-icon.received  { background: rgba(239,68,68,0.15); color: var(--color-danger); }
    .lifecycle-icon.claimed   { background: rgba(99,102,241,0.15); color: var(--color-info); }
    .lifecycle-icon.escalated { background: rgba(245,158,11,0.15); color: var(--color-warn); }
    .lifecycle-icon.resolved  { background: rgba(16,185,129,0.15); color: var(--color-success); }
    .lifecycle-label { font-size: 11px; font-weight: 700; letter-spacing: 0.07em; color: var(--color-text-dim); }
    .lifecycle-arrow { color: var(--color-muted); font-size: 0.9rem; }
    .lifecycle-note { margin-top: 1rem; font-size: 0.8rem; color: var(--color-text-dim); text-align: center; line-height: 1.6; border-top: 1px solid var(--border-subtle); padding-top: 1rem; }

    .refresh-label { display: flex; align-items: center; gap: 0.4rem; font-size: 0.78rem; color: var(--color-text-dim); }
    .last-refreshed { font-size: 0.75rem; color: var(--color-muted); background: var(--bg-elevated); padding: 4px 10px; border-radius: 6px; }
  `]
})
export class DashboardComponent implements OnInit, OnDestroy {
  alerts: Alert[] = [];
  criticalAlerts: Alert[] = [];
  loading = false;
  lastRefreshed = new Date();

  stats = { received: 0, claimed: 0, escalated: 0, resolved: 0 };

  chartData: any = {};
  chartOptions: any = {};
  legendItems: { label: string; color: string; count: number }[] = [];

  private sub?: Subscription;

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.sub = interval(30000).pipe(
      startWith(0),
      switchMap(() => {
        this.loading = true;
        return this.alertService.getAll();
      })
    ).subscribe({
      next: (alerts) => {
        this.alerts = alerts;
        this.loading = false;
        this.lastRefreshed = new Date();
        this.computeStats();
        this.buildChart();
        this.criticalAlerts = alerts
          .filter(a => a.status === 'RECEIVED' || a.status === 'ESCALATED' || a.status === 'CLAIMED')
          .sort((a, b) => {
            const order: Record<AlertStatus, number> = { RECEIVED: 0, ESCALATED: 1, CLAIMED: 2, RESOLVED: 3 };
            return order[a.status] - order[b.status];
          })
          .slice(0, 8);
      },
      error: () => { this.loading = false; }
    });
  }

  ngOnDestroy(): void { this.sub?.unsubscribe(); }

  private computeStats(): void {
    this.stats = {
      received:  this.alerts.filter(a => a.status === 'RECEIVED').length,
      claimed:   this.alerts.filter(a => a.status === 'CLAIMED').length,
      escalated: this.alerts.filter(a => a.status === 'ESCALATED').length,
      resolved:  this.alerts.filter(a => a.status === 'RESOLVED').length,
    };
  }

  private buildChart(): void {
    const colors = {
      RECEIVED:  '#ef4444',
      CLAIMED:   '#6366f1',
      ESCALATED: '#f59e0b',
      RESOLVED:  '#10b981',
    };

    this.legendItems = [
      { label: 'Received',  color: colors.RECEIVED,  count: this.stats.received },
      { label: 'Claimed',   color: colors.CLAIMED,   count: this.stats.claimed },
      { label: 'Escalated', color: colors.ESCALATED, count: this.stats.escalated },
      { label: 'Resolved',  color: colors.RESOLVED,  count: this.stats.resolved },
    ];

    this.chartData = {
      labels: ['Received', 'Claimed', 'Escalated', 'Resolved'],
      datasets: [{
        data: [this.stats.received, this.stats.claimed, this.stats.escalated, this.stats.resolved],
        backgroundColor: [colors.RECEIVED, colors.CLAIMED, colors.ESCALATED, colors.RESOLVED],
        borderColor: 'transparent',
        hoverOffset: 6
      }]
    };

    this.chartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '72%',
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx: any) => ` ${ctx.label}: ${ctx.parsed} alert(s)`
          }
        }
      }
    };
  }
}
