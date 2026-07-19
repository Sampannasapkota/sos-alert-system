import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { MessageService, ConfirmationService } from 'primeng/api';
import { AlertService } from '../../core/services/alert.service';
import { DeviceService } from '../../core/services/device.service';
import { Alert, AlertStatus, Device } from '../../core/models/models';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    TableModule, ButtonModule, DialogModule, InputTextModule, InputNumberModule,
    TagModule, TooltipModule, ConfirmDialogModule, CalendarModule, DropdownModule
  ],
  template: `
    <div class="page-header">
      <h1>🚨 SOS Alerts</h1>
      <p>Monitor, claim, and resolve SOS alerts from GPS tracking devices</p>
    </div>

    <!-- Filter Bar -->
    <div class="card" style="margin-bottom:1rem; padding:1rem 1.5rem;">
      <div style="display:flex; gap:1rem; align-items:center; flex-wrap:wrap;">
        <span class="p-input-icon-left" style="flex:1; min-width:220px;">
          <i class="pi pi-search"></i>
          <input pInputText [(ngModel)]="globalFilter" placeholder="Search device, trek, group..."
                 style="width:100%;" (input)="dt.filterGlobal(globalFilter,'contains')"/>
        </span>
        <div class="status-filters">
          <button *ngFor="let s of statusFilters"
            class="status-filter-btn"
            [class.active]="activeFilter === s.value"
            [style.border-color]="s.color"
            [style.color]="activeFilter === s.value ? '#fff' : s.color"
            [style.background]="activeFilter === s.value ? s.color : 'transparent'"
            (click)="filterByStatus(s.value)">
            {{ s.label }}
          </button>
        </div>
        <p-button label="New SOS Alert" icon="pi pi-plus" (onClick)="openNewAlert()"></p-button>
      </div>
    </div>

    <div class="card" style="padding:0; overflow:hidden;">
      <p-table #dt
        [value]="alerts"
        [paginator]="true"
        [rows]="15"
        [rowsPerPageOptions]="[10,15,25,50]"
        [globalFilterFields]="['deviceCode','trekName','trekGroupName','orderReference','status','claimedBy']"
        [loading]="loading"
        [rowHover]="true"
        [showCurrentPageReport]="true"
        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} alerts"
        styleClass="p-datatable-sm p-datatable-gridlines"
        [tableStyle]="{'min-width':'1000px'}">

        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px">#</th>
            <th pSortableColumn="deviceCode">Device <p-sortIcon field="deviceCode"/></th>
            <th>Trek / Group</th>
            <th>Coordinates</th>
            <th pSortableColumn="alertTimestamp">Alert Time <p-sortIcon field="alertTimestamp"/></th>
            <th pSortableColumn="status">Status <p-sortIcon field="status"/></th>
            <th>Claimed By</th>
            <th style="width:160px; text-align:center;">Actions</th>
          </tr>
        </ng-template>

        <ng-template pTemplate="body" let-alert>
          <tr [ngClass]="getRowClass(alert)">
            <td><span class="row-id">#{{ alert.id }}</span></td>
            <td>
              <div class="device-cell">
                <div class="device-code">{{ alert.deviceCode }}</div>
                <div class="device-name">{{ alert.deviceDisplayName }}</div>
              </div>
            </td>
            <td>
              <div class="trek-cell">
                <div class="trek-name">{{ alert.trekName }}</div>
                <div class="trek-group">{{ alert.trekGroupName }}</div>
              </div>
            </td>
            <td>
              <div class="coord-cell">
                <span class="coord">{{ alert.latitude | number:'1.4-4' }}°N</span>
                <span class="coord">{{ alert.longitude | number:'1.4-4' }}°E</span>
              </div>
            </td>
            <td>
              <div class="time-cell">{{ alert.alertTimestamp | date:'MMM d, y HH:mm:ss' }}</div>
            </td>
            <td>
              <span class="status-badge" [ngClass]="'status-' + alert.status.toLowerCase()">
                <span class="status-dot" [ngClass]="'dot-' + alert.status.toLowerCase()"></span>
                {{ alert.status }}
              </span>
            </td>
            <td>
              <span *ngIf="alert.claimedBy" class="claimed-by">
                <i class="pi pi-user" style="font-size:0.75rem;"></i>
                {{ alert.claimedBy }}
              </span>
              <span *ngIf="!alert.claimedBy" class="not-claimed">—</span>
            </td>
            <td>
              <div class="action-buttons">
                <p-button
                  *ngIf="alert.status === 'RECEIVED' || alert.status === 'ESCALATED'"
                  label="Claim"
                  icon="pi pi-user-plus"
                  styleClass="p-button-sm p-button-warning"
                  [pTooltip]="'Claim this alert'"
                  (onClick)="openClaimDialog(alert)">
                </p-button>
                <p-button
                  *ngIf="alert.status === 'CLAIMED'"
                  label="Resolve"
                  icon="pi pi-check"
                  styleClass="p-button-sm p-button-success"
                  [pTooltip]="'Mark as resolved'"
                  (onClick)="confirmResolve(alert)">
                </p-button>
                <p-button
                  *ngIf="alert.status === 'RESOLVED' || alert.status === 'ESCALATED'"
                  icon="pi pi-eye"
                  styleClass="p-button-sm p-button-text"
                  [pTooltip]="'View details'"
                  (onClick)="viewDetails(alert)">
                </p-button>
              </div>
            </td>
          </tr>
        </ng-template>

        <ng-template pTemplate="emptymessage">
          <tr><td colspan="8" style="text-align:center; padding:3rem; color:var(--color-text-dim);">
            <i class="pi pi-bell" style="font-size:2.5rem; display:block; margin-bottom:0.75rem;"></i>
            No alerts found
          </td></tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Claim Dialog -->
    <p-dialog header="Claim Alert" [(visible)]="claimDialogVisible"
              [modal]="true" [style]="{width:'420px'}" [closable]="true">
      <div *ngIf="selectedAlert" class="dialog-content">
        <div class="alert-summary">
          <div class="summary-row">
            <span class="summary-label">Device:</span>
            <span class="summary-value">{{ selectedAlert.deviceCode }}</span>
          </div>
          <div class="summary-row">
            <span class="summary-label">Trek:</span>
            <span class="summary-value">{{ selectedAlert.trekName }}</span>
          </div>
          <div class="summary-row">
            <span class="summary-label">Status:</span>
            <span class="status-badge" [ngClass]="'status-' + selectedAlert.status.toLowerCase()">{{ selectedAlert.status }}</span>
          </div>
        </div>
        <div class="field">
          <label class="field-label" for="coordinatorName">Your Name (Coordinator) *</label>
          <input id="coordinatorName" pInputText [(ngModel)]="coordinatorName"
                 placeholder="e.g. John Smith"
                 style="width:100%; margin-top:0.4rem;"
                 (keydown.enter)="submitClaim()"/>
          <small class="field-hint">This will be recorded as the responder for this alert.</small>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="claimDialogVisible=false"></p-button>
        <p-button label="Claim Alert" icon="pi pi-user-plus" styleClass="p-button-warning"
                  [disabled]="!coordinatorName.trim()" [loading]="actionLoading"
                  (onClick)="submitClaim()"></p-button>
      </ng-template>
    </p-dialog>

    <!-- New SOS Alert Dialog -->
    <p-dialog header="📡 Submit New SOS Alert" [(visible)]="newAlertDialogVisible"
              [modal]="true" [style]="{width:'480px'}">
      <form [formGroup]="newAlertForm" class="dialog-content">
        <div class="field">
          <label class="field-label">Device *</label>
          <p-dropdown formControlName="deviceId" [options]="devices"
                      optionLabel="deviceId" optionValue="id"
                      placeholder="Select device" [style]="{width:'100%'}" appendTo="body">
          </p-dropdown>
        </div>
        <div class="field-row">
          <div class="field">
            <label class="field-label">Latitude *</label>
            <input pInputText formControlName="latitude" placeholder="27.9881000" style="width:100%;"/>
          </div>
          <div class="field">
            <label class="field-label">Longitude *</label>
            <input pInputText formControlName="longitude" placeholder="86.9250000" style="width:100%;"/>
          </div>
        </div>
        <div class="field">
          <label class="field-label">Alert Timestamp *</label>
          <p-calendar formControlName="timestamp" [showTime]="true" hourFormat="24"
                      [style]="{width:'100%'}" dateFormat="yy-mm-dd" appendTo="body">
          </p-calendar>
        </div>
      </form>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="newAlertDialogVisible=false"></p-button>
        <p-button label="Submit Alert" icon="pi pi-send"
                  [disabled]="newAlertForm.invalid" [loading]="actionLoading"
                  (onClick)="submitNewAlert()"></p-button>
      </ng-template>
    </p-dialog>

    <!-- Detail Dialog -->
    <p-dialog header="Alert Details" [(visible)]="detailDialogVisible"
              [modal]="true" [style]="{width:'520px'}">
      <div *ngIf="selectedAlert" class="detail-grid">
        <div class="detail-row"><span>ID:</span><strong>#{{ selectedAlert.id }}</strong></div>
        <div class="detail-row"><span>Device:</span><strong>{{ selectedAlert.deviceCode }} — {{ selectedAlert.deviceDisplayName }}</strong></div>
        <div class="detail-row"><span>Order:</span><strong>{{ selectedAlert.orderReference }}</strong></div>
        <div class="detail-row"><span>Trek:</span><strong>{{ selectedAlert.trekName }}</strong></div>
        <div class="detail-row"><span>Group:</span><strong>{{ selectedAlert.trekGroupName }}</strong></div>
        <div class="detail-row"><span>Coordinates:</span><strong>{{ selectedAlert.latitude }}, {{ selectedAlert.longitude }}</strong></div>
        <div class="detail-row"><span>Alert Time:</span><strong>{{ selectedAlert.alertTimestamp | date:'full' }}</strong></div>
        <div class="detail-row"><span>Status:</span><span class="status-badge" [ngClass]="'status-' + selectedAlert.status.toLowerCase()">{{ selectedAlert.status }}</span></div>
        <div class="detail-row" *ngIf="selectedAlert.claimedBy"><span>Claimed By:</span><strong>{{ selectedAlert.claimedBy }}</strong></div>
        <div class="detail-row" *ngIf="selectedAlert.claimedAt"><span>Claimed At:</span><strong>{{ selectedAlert.claimedAt | date:'medium' }}</strong></div>
        <div class="detail-row" *ngIf="selectedAlert.escalatedAt"><span>Escalated At:</span><strong>{{ selectedAlert.escalatedAt | date:'medium' }}</strong></div>
        <div class="detail-row" *ngIf="selectedAlert.resolvedAt"><span>Resolved At:</span><strong>{{ selectedAlert.resolvedAt | date:'medium' }}</strong></div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Close" styleClass="p-button-text" (onClick)="detailDialogVisible=false"></p-button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .status-filters { display:flex; gap:0.4rem; flex-wrap:wrap; }
    .status-filter-btn {
      padding: 4px 12px; border-radius: 20px; border: 1px solid;
      cursor: pointer; font-size: 11px; font-weight: 700;
      letter-spacing: 0.05em; transition: all 0.2s; background: transparent;
    }
    .status-filter-btn:hover { opacity: 0.8; }

    .row-id { font-size: 11px; color: var(--color-muted); font-family: monospace; }
    .device-code { font-weight: 700; font-size: 0.875rem; }
    .device-name { font-size: 0.75rem; color: var(--color-text-dim); }
    .trek-name   { font-weight: 600; font-size: 0.875rem; }
    .trek-group  { font-size: 0.75rem; color: var(--color-text-dim); }
    .coord-cell  { display:flex; flex-direction:column; gap:2px; }
    .coord       { font-family: monospace; font-size: 0.75rem; color: var(--color-text-dim); }
    .time-cell   { font-size: 0.8rem; white-space: nowrap; }

    .status-badge {
      display: inline-flex; align-items: center; gap: 5px;
      font-size: 10px; font-weight: 700; letter-spacing: 0.07em;
      padding: 3px 10px; border-radius: 20px;
    }
    .status-received  { background: rgba(239,68,68,0.15);  color: var(--color-danger); border: 1px solid rgba(239,68,68,0.3); }
    .status-claimed   { background: rgba(99,102,241,0.15); color: var(--color-info);   border: 1px solid rgba(99,102,241,0.3); }
    .status-escalated { background: rgba(245,158,11,0.15); color: var(--color-warn);   border: 1px solid rgba(245,158,11,0.3); }
    .status-resolved  { background: rgba(16,185,129,0.15); color: var(--color-success);border: 1px solid rgba(16,185,129,0.3); }

    .status-dot {
      width: 6px; height: 6px; border-radius: 50%;
    }
    .dot-received  { background: var(--color-danger);  animation: blink 1s infinite; }
    .dot-escalated { background: var(--color-warn);    animation: blink 1.5s infinite; }
    .dot-claimed   { background: var(--color-info); }
    .dot-resolved  { background: var(--color-success); }
    @keyframes blink { 0%,100%{opacity:1;} 50%{opacity:0.2;} }

    .claimed-by { display:flex; align-items:center; gap:0.35rem; font-size:0.8rem; color:var(--color-info); }
    .not-claimed { color: var(--color-muted); }
    .action-buttons { display:flex; gap:0.4rem; justify-content:center; flex-wrap:wrap; }

    .dialog-content { display:flex; flex-direction:column; gap:1rem; }
    .alert-summary {
      background: var(--bg-elevated); border-radius: 8px; padding: 0.85rem 1rem;
      border: 1px solid var(--border-medium);
      display: flex; flex-direction: column; gap: 0.5rem; margin-bottom: 0.5rem;
    }
    .summary-row { display:flex; align-items:center; gap:0.75rem; }
    .summary-label { font-size:0.78rem; color:var(--color-text-dim); width:60px; flex-shrink:0; }
    .summary-value { font-weight:600; font-size:0.875rem; }

    .field { display:flex; flex-direction:column; }
    .field-label { font-size:0.8rem; font-weight:600; color:var(--color-text-dim); margin-bottom:0.35rem; }
    .field-hint { font-size:0.72rem; color:var(--color-muted); margin-top:0.3rem; }
    .field-row { display:grid; grid-template-columns:1fr 1fr; gap:1rem; }

    .detail-grid { display:flex; flex-direction:column; gap:0.6rem; }
    .detail-row { display:flex; gap:1rem; align-items:center; padding: 0.5rem 0; border-bottom: 1px solid var(--border-subtle); font-size:0.875rem; }
    .detail-row span:first-child { width:110px; flex-shrink:0; color:var(--color-text-dim); font-weight:500; }
  `]
})
export class AlertsComponent implements OnInit {
  alerts: Alert[] = [];
  loading = false;
  globalFilter = '';
  activeFilter: AlertStatus | 'ALL' = 'ALL';

  devices: Device[] = [];

  claimDialogVisible = false;
  newAlertDialogVisible = false;
  detailDialogVisible = false;
  selectedAlert: Alert | null = null;
  coordinatorName = '';
  actionLoading = false;
  newAlertForm: FormGroup;

  statusFilters = [
    { label: 'All',       value: 'ALL'       as AlertStatus | 'ALL', color: '#94a3b8' },
    { label: 'Received',  value: 'RECEIVED'  as AlertStatus | 'ALL', color: '#ef4444' },
    { label: 'Claimed',   value: 'CLAIMED'   as AlertStatus | 'ALL', color: '#6366f1' },
    { label: 'Escalated', value: 'ESCALATED' as AlertStatus | 'ALL', color: '#f59e0b' },
    { label: 'Resolved',  value: 'RESOLVED'  as AlertStatus | 'ALL', color: '#10b981' },
  ];

  constructor(
    private alertService: AlertService,
    private deviceService: DeviceService,
    private messageService: MessageService,
    private confirmService: ConfirmationService,
    private fb: FormBuilder
  ) {
    this.newAlertForm = this.fb.group({
      deviceId:  [null, Validators.required],
      latitude:  ['',   Validators.required],
      longitude: ['',   Validators.required],
      timestamp: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.load();
    this.deviceService.getAll().subscribe(d => this.devices = d);
  }

  load(): void {
    this.loading = true;
    this.alertService.getAll().subscribe({
      next: a => { this.alerts = a; this.loading = false; },
      error: () => this.loading = false
    });
  }

  filterByStatus(status: AlertStatus | 'ALL'): void {
    this.activeFilter = status;
    // PrimeTable custom filtering is handled by the global filter + programmatic
    // For real status filtering we re-set items based on status
    this.alertService.getAll().subscribe(all => {
      this.alerts = status === 'ALL' ? all : all.filter(a => a.status === status);
    });
  }

  getRowClass(alert: Alert): string {
    if (alert.status === 'RECEIVED')  return 'row-received';
    if (alert.status === 'ESCALATED') return 'row-escalated';
    return '';
  }

  openClaimDialog(alert: Alert): void {
    this.selectedAlert = alert;
    this.coordinatorName = '';
    this.claimDialogVisible = true;
  }

  submitClaim(): void {
    if (!this.selectedAlert || !this.coordinatorName.trim()) return;
    this.actionLoading = true;
    this.alertService.claim(this.selectedAlert.id, { coordinatorName: this.coordinatorName }).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Alert Claimed', detail: `Alert #${this.selectedAlert!.id} claimed by ${this.coordinatorName}` });
        this.claimDialogVisible = false;
        this.actionLoading = false;
        this.load();
      },
      error: () => this.actionLoading = false
    });
  }

  confirmResolve(alert: Alert): void {
    this.confirmService.confirm({
      message: `Resolve alert #${alert.id} from device <strong>${alert.deviceCode}</strong>?<br><small>Trek: ${alert.trekName}</small>`,
      header: 'Confirm Resolution',
      icon: 'pi pi-check-circle',
      accept: () => this.resolveAlert(alert)
    });
  }

  resolveAlert(alert: Alert): void {
    this.alertService.resolve(alert.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Alert Resolved', detail: `Alert #${alert.id} has been resolved.` });
        this.load();
      }
    });
  }

  viewDetails(alert: Alert): void {
    this.selectedAlert = alert;
    this.detailDialogVisible = true;
  }

  openNewAlert(): void {
    this.newAlertForm.reset({
      deviceId: 1,
      latitude: '27.9881',
      longitude: '86.9250',
      timestamp: new Date()
    });
    this.newAlertDialogVisible = true;
  }

  submitNewAlert(): void {
    if (this.newAlertForm.invalid) return;
    this.actionLoading = true;
    const val = this.newAlertForm.value;
    const ts: Date = val.timestamp;
    const pad = (n: number) => String(n).padStart(2, '0');
    const isoTs = `${ts.getFullYear()}-${pad(ts.getMonth()+1)}-${pad(ts.getDate())}T${pad(ts.getHours())}:${pad(ts.getMinutes())}:${pad(ts.getSeconds())}`;

    this.alertService.create({
      deviceId: val.deviceId,
      latitude: parseFloat(val.latitude),
      longitude: parseFloat(val.longitude),
      timestamp: isoTs
    }).subscribe({
      next: (a) => {
        this.messageService.add({ severity: 'success', summary: 'Alert Created', detail: `SOS Alert #${a.id} submitted.` });
        this.newAlertDialogVisible = false;
        this.actionLoading = false;
        this.load();
      },
      error: () => this.actionLoading = false
    });
  }
}
