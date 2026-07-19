import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService, ConfirmationService } from 'primeng/api';
import { DeviceService } from '../../core/services/device.service';
import { Device } from '../../core/models/models';

@Component({
  selector: 'app-devices',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, DialogModule, InputTextModule, TagModule, TooltipModule],
  template: `
    <div class="page-header">
      <div style="display:flex;align-items:center;justify-content:space-between;">
        <div>
          <h1>📱 Devices</h1>
          <p>Manage GPS/satellite tracking devices used in expeditions</p>
        </div>
        <p-button label="Add Device" icon="pi pi-plus" (onClick)="openAdd()"></p-button>
      </div>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
      <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border-subtle);">
        <span class="p-input-icon-left">
          <i class="pi pi-search"></i>
          <input pInputText [(ngModel)]="filter" placeholder="Search devices..." (input)="dt.filterGlobal(filter,'contains')"/>
        </span>
      </div>
      <p-table #dt [value]="devices" [loading]="loading" [paginator]="true" [rows]="15"
               [globalFilterFields]="['deviceId','displayName']"
               [rowHover]="true" styleClass="p-datatable-sm"
               [showCurrentPageReport]="true" currentPageReportTemplate="{first}-{last} of {totalRecords}">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px">#</th>
            <th pSortableColumn="deviceId">Device Code <p-sortIcon field="deviceId"/></th>
            <th>Display Name</th>
            <th>Status</th>
            <th pSortableColumn="createdAt">Created <p-sortIcon field="createdAt"/></th>
            <th style="width:130px;text-align:center;">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-device>
          <tr>
            <td><span style="font-size:11px;color:var(--color-muted);font-family:monospace;">#{{ device.id }}</span></td>
            <td><strong style="font-family:monospace;">{{ device.deviceId }}</strong></td>
            <td>{{ device.displayName || '—' }}</td>
            <td>
              <p-tag [value]="device.active ? 'Active' : 'Inactive'"
                     [severity]="device.active ? 'success' : 'danger'"></p-tag>
            </td>
            <td style="font-size:0.8rem;color:var(--color-text-dim);">{{ device.createdAt | date:'MMM d, y' }}</td>
            <td>
              <div style="display:flex;gap:0.4rem;justify-content:center;">
                <p-button icon="pi pi-pencil" styleClass="p-button-sm p-button-text" pTooltip="Edit" (onClick)="openEdit(device)"></p-button>
                <p-button icon="pi pi-trash" styleClass="p-button-sm p-button-text p-button-danger" pTooltip="Delete" (onClick)="confirmDelete(device)"></p-button>
              </div>
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="6" style="text-align:center;padding:3rem;color:var(--color-text-dim);">No devices found</td></tr>
        </ng-template>
      </p-table>
    </div>

    <p-dialog [header]="editMode ? 'Edit Device' : 'Add Device'" [(visible)]="dialogVisible"
              [modal]="true" [style]="{width:'400px'}">
      <div class="dialog-content" style="display:flex;flex-direction:column;gap:1rem;">
        <div class="field">
          <label class="field-label">Device Code *</label>
          <input pInputText [(ngModel)]="form.deviceCode" placeholder="e.g. GPS-001" style="width:100%;margin-top:0.35rem;"/>
        </div>
        <div class="field">
          <label class="field-label">Display Name</label>
          <input pInputText [(ngModel)]="form.displayName" placeholder="e.g. Garmin InReach Mini" style="width:100%;margin-top:0.35rem;"/>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="dialogVisible=false"></p-button>
        <p-button [label]="editMode ? 'Update' : 'Create'" icon="pi pi-save" [loading]="saving"
                  [disabled]="!form.deviceCode.trim()" (onClick)="save()"></p-button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`.field-label{font-size:.8rem;font-weight:600;color:var(--color-text-dim);}`]
})
export class DevicesComponent implements OnInit {
  devices: Device[] = [];
  loading = false;
  filter = '';
  dialogVisible = false;
  editMode = false;
  saving = false;
  selectedId?: number;
  form = { deviceCode: '', displayName: '' };

  constructor(
    private svc: DeviceService,
    private messageService: MessageService,
    private confirmService: ConfirmationService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({ next: d => { this.devices = d; this.loading = false; }, error: () => this.loading = false });
  }

  openAdd(): void { this.editMode = false; this.form = { deviceCode: '', displayName: '' }; this.dialogVisible = true; }

  openEdit(d: Device): void {
    this.editMode = true; this.selectedId = d.id;
    this.form = { deviceCode: d.deviceId, displayName: d.displayName };
    this.dialogVisible = true;
  }

  save(): void {
    this.saving = true;
    const obs = this.editMode
      ? this.svc.update(this.selectedId!, this.form)
      : this.svc.create(this.form);
    obs.subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Saved', detail: `Device ${this.editMode ? 'updated' : 'created'}.` });
        this.dialogVisible = false; this.saving = false; this.load();
      },
      error: () => this.saving = false
    });
  }

  confirmDelete(d: Device): void {
    this.confirmService.confirm({
      message: `Delete device <strong>${d.deviceId}</strong>?`,
      header: 'Confirm Delete', icon: 'pi pi-trash',
      accept: () => this.svc.delete(d.id).subscribe(() => {
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Device removed.' });
        this.load();
      })
    });
  }
}
