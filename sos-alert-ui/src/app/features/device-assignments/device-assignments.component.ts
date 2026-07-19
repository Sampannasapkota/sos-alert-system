import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { MessageService, ConfirmationService } from 'primeng/api';
import { DeviceAssignmentService } from '../../core/services/device-assignment.service';
import { DeviceService } from '../../core/services/device.service';
import { OrderService } from '../../core/services/order.service';
import { DeviceAssignment, Device, Order } from '../../core/models/models';

@Component({
  selector: 'app-device-assignments',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, DialogModule, InputTextModule, TagModule, TooltipModule, DropdownModule, CalendarModule],
  template: `
    <div class="page-header">
      <div style="display:flex;align-items:center;justify-content:space-between;">
        <div>
          <h1>🔗 Device Assignments</h1>
          <p>Assign GPS devices to trek orders for SOS alert resolution</p>
        </div>
        <p-button label="New Assignment" icon="pi pi-plus" (onClick)="openAdd()"></p-button>
      </div>
    </div>

    <!-- Info Banner -->
    <div class="info-banner">
      <i class="pi pi-info-circle"></i>
      <span>A device may be assigned to multiple orders over time, but only one at a time. Overlapping assignments are rejected. Assignments determine which order "owns" an incoming SOS alert.</span>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
      <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border-subtle);">
        <span class="p-input-icon-left">
          <i class="pi pi-search"></i>
          <input pInputText [(ngModel)]="filter" placeholder="Search assignments..." (input)="dt.filterGlobal(filter,'contains')"/>
        </span>
      </div>
      <p-table #dt [value]="assignments" [loading]="loading" [paginator]="true" [rows]="15"
               [globalFilterFields]="['deviceCode','orderReference','trekName','trekGroupName']"
               [rowHover]="true" styleClass="p-datatable-sm"
               [showCurrentPageReport]="true" currentPageReportTemplate="{first}-{last} of {totalRecords}">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px">#</th>
            <th pSortableColumn="deviceCode">Device <p-sortIcon field="deviceCode"/></th>
            <th pSortableColumn="orderReference">Order <p-sortIcon field="orderReference"/></th>
            <th>Trek / Group</th>
            <th>Assigned From</th>
            <th>Assigned Until</th>
            <th>Status</th>
            <th style="width:130px;text-align:center;">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-a>
          <tr>
            <td><span style="font-size:11px;color:var(--color-muted);font-family:monospace;">#{{ a.id }}</span></td>
            <td>
              <div style="display:flex;flex-direction:column;">
                <strong style="font-family:monospace;">{{ a.deviceCode }}</strong>
                <span style="font-size:0.72rem;color:var(--color-text-dim);">{{ a.deviceDisplayName }}</span>
              </div>
            </td>
            <td><strong style="font-family:monospace;">{{ a.orderReference }}</strong></td>
            <td>
              <div style="display:flex;flex-direction:column;">
                <span style="font-weight:600;font-size:0.85rem;">{{ a.trekName }}</span>
                <span style="font-size:0.72rem;color:var(--color-text-dim);">{{ a.trekGroupName }}</span>
              </div>
            </td>
            <td style="font-size:0.8rem;white-space:nowrap;">{{ a.assignedFrom | date:'MMM d, y HH:mm' }}</td>
            <td style="font-size:0.8rem;white-space:nowrap;">{{ a.assignedUntil ? (a.assignedUntil | date:'MMM d, y HH:mm') : '(open)' }}</td>
            <td><p-tag [value]="a.active ? 'Active' : 'Inactive'" [severity]="a.active ? 'success' : 'danger'"></p-tag></td>
            <td>
              <div style="display:flex;gap:0.4rem;justify-content:center;">
                <p-button icon="pi pi-pencil" styleClass="p-button-sm p-button-text" pTooltip="Edit" (onClick)="openEdit(a)"></p-button>
                <p-button icon="pi pi-trash" styleClass="p-button-sm p-button-text p-button-danger" pTooltip="Delete" (onClick)="confirmDelete(a)"></p-button>
              </div>
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="8" style="text-align:center;padding:3rem;color:var(--color-text-dim);">No device assignments found</td></tr>
        </ng-template>
      </p-table>
    </div>

    <p-dialog [header]="editMode ? 'Edit Assignment' : 'New Device Assignment'" [(visible)]="dialogVisible"
              [modal]="true" [style]="{width:'520px'}">
      <div style="display:flex;flex-direction:column;gap:1rem;">
        <div class="field">
          <label class="field-label">Device *</label>
          <p-dropdown [(ngModel)]="form.deviceId" [options]="devices"
                      optionLabel="deviceId" optionValue="id"
                      placeholder="Select device" [style]="{width:'100%'}" appendTo="body">
          </p-dropdown>
        </div>
        <div class="field">
          <label class="field-label">Order *</label>
          <p-dropdown [(ngModel)]="form.orderId" [options]="orders"
                      optionLabel="orderReference" optionValue="id"
                      placeholder="Select order" [style]="{width:'100%'}" appendTo="body">
          </p-dropdown>
        </div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
          <div class="field">
            <label class="field-label">Assigned From *</label>
            <p-calendar [(ngModel)]="form.assignedFrom" [showTime]="true" hourFormat="24"
                        [style]="{width:'100%',marginTop:'0.35rem'}" appendTo="body">
            </p-calendar>
          </div>
          <div class="field">
            <label class="field-label">Assigned Until <span style="color:var(--color-muted);font-weight:400;">(optional)</span></label>
            <p-calendar [(ngModel)]="form.assignedUntil" [showTime]="true" hourFormat="24"
                        [style]="{width:'100%',marginTop:'0.35rem'}" appendTo="body">
            </p-calendar>
          </div>
        </div>
        <div style="font-size:0.78rem;color:var(--color-muted);background:var(--bg-elevated);padding:0.75rem;border-radius:8px;border:1px solid var(--border-subtle);">
          <i class="pi pi-info-circle" style="margin-right:0.4rem;"></i>
          Leave "Assigned Until" empty for an open-ended assignment. The system will reject overlapping assignments for the same device.
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="dialogVisible=false"></p-button>
        <p-button [label]="editMode ? 'Update' : 'Create'" icon="pi pi-save" [loading]="saving"
                  [disabled]="!form.deviceId || !form.orderId || !form.assignedFrom"
                  (onClick)="save()"></p-button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .field-label{font-size:.8rem;font-weight:600;color:var(--color-text-dim);}
    .info-banner {
      display:flex; align-items:flex-start; gap:0.6rem;
      background: rgba(99,102,241,0.08); border: 1px solid rgba(99,102,241,0.25);
      border-radius: 8px; padding: 0.85rem 1rem; margin-bottom: 1rem;
      font-size: 0.82rem; color: var(--color-text-dim); line-height: 1.5;
    }
    .info-banner i { color: var(--color-info); margin-top: 1px; flex-shrink: 0; }
  `]
})
export class DeviceAssignmentsComponent implements OnInit {
  assignments: DeviceAssignment[] = [];
  devices: Device[] = [];
  orders: Order[] = [];
  loading = false;
  filter = '';
  dialogVisible = false;
  editMode = false;
  saving = false;
  selectedId?: number;
  form: any = { deviceId: null, orderId: null, assignedFrom: null, assignedUntil: null };

  constructor(
    private svc: DeviceAssignmentService,
    private deviceSvc: DeviceService,
    private orderSvc: OrderService,
    private messageService: MessageService,
    private confirmService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.load();
    this.deviceSvc.getAll().subscribe(d => this.devices = d);
    this.orderSvc.getAll().subscribe(o => this.orders = o);
  }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({ next: a => { this.assignments = a; this.loading = false; }, error: () => this.loading = false });
  }

  openAdd(): void { this.editMode = false; this.form = { deviceId: null, orderId: null, assignedFrom: null, assignedUntil: null }; this.dialogVisible = true; }

  openEdit(a: DeviceAssignment): void {
    this.editMode = true; this.selectedId = a.id;
    this.form = {
      deviceId: a.deviceId, orderId: a.orderId,
      assignedFrom: a.assignedFrom ? new Date(a.assignedFrom) : null,
      assignedUntil: a.assignedUntil ? new Date(a.assignedUntil) : null
    };
    this.dialogVisible = true;
  }

  private toIso(d: Date | null): string | undefined {
    if (!d) return undefined;
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
  }

  save(): void {
    this.saving = true;
    const payload: any = {
      deviceId: this.form.deviceId,
      orderId: this.form.orderId,
      assignedFrom: this.toIso(this.form.assignedFrom),
    };
    if (this.form.assignedUntil) payload.assignedUntil = this.toIso(this.form.assignedUntil);
    const obs = this.editMode ? this.svc.update(this.selectedId!, payload) : this.svc.create(payload);
    obs.subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Saved', detail: `Assignment ${this.editMode ? 'updated' : 'created'}.` }); this.dialogVisible = false; this.saving = false; this.load(); },
      error: () => this.saving = false
    });
  }

  confirmDelete(a: DeviceAssignment): void {
    this.confirmService.confirm({
      message: `Delete assignment for device <strong>${a.deviceCode}</strong> on order <strong>${a.orderReference}</strong>?`,
      header: 'Confirm Delete', icon: 'pi pi-trash',
      accept: () => this.svc.delete(a.id).subscribe(() => { this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Assignment removed.' }); this.load(); })
    });
  }
}
