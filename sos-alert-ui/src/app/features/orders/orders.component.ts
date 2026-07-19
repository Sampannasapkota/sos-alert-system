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
import { OrderService } from '../../core/services/order.service';
import { TrekGroupService } from '../../core/services/trek-group.service';
import { Order, OrderStatus, TrekGroup } from '../../core/models/models';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, DialogModule, InputTextModule, TagModule, TooltipModule, DropdownModule, CalendarModule],
  template: `
    <div class="page-header">
      <div style="display:flex;align-items:center;justify-content:space-between;">
        <div>
          <h1>📋 Orders</h1>
          <p>Trek orders linking groups to expedition dates</p>
        </div>
        <p-button label="New Order" icon="pi pi-plus" (onClick)="openAdd()"></p-button>
      </div>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
      <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border-subtle);">
        <span class="p-input-icon-left">
          <i class="pi pi-search"></i>
          <input pInputText [(ngModel)]="filter" placeholder="Search orders..." (input)="dt.filterGlobal(filter,'contains')"/>
        </span>
      </div>
      <p-table #dt [value]="orders" [loading]="loading" [paginator]="true" [rows]="15"
               [globalFilterFields]="['orderReference','trekName','trekGroupName','status']"
               [rowHover]="true" styleClass="p-datatable-sm"
               [showCurrentPageReport]="true" currentPageReportTemplate="{first}-{last} of {totalRecords}">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px">#</th>
            <th pSortableColumn="orderReference">Reference <p-sortIcon field="orderReference"/></th>
            <th pSortableColumn="trekName">Trek Name <p-sortIcon field="trekName"/></th>
            <th>Trek Group</th>
            <th>Duration</th>
            <th pSortableColumn="status">Status <p-sortIcon field="status"/></th>
            <th style="width:130px;text-align:center;">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-o>
          <tr>
            <td><span style="font-size:11px;color:var(--color-muted);font-family:monospace;">#{{ o.id }}</span></td>
            <td><strong style="font-family:monospace;">{{ o.orderReference }}</strong></td>
            <td>{{ o.trekName }}</td>
            <td>
              <div style="display:flex;flex-direction:column;">
                <span style="font-weight:600;font-size:0.85rem;">{{ o.trekGroupName }}</span>
                <span style="font-size:0.72rem;color:var(--color-text-dim);font-family:monospace;">{{ o.trekGroupCode }}</span>
              </div>
            </td>
            <td style="font-size:0.8rem;white-space:nowrap;">{{ o.startDate }} → {{ o.endDate }}</td>
            <td>
              <span class="order-status" [ngClass]="'os-' + o.status.toLowerCase()">{{ o.status }}</span>
            </td>
            <td>
              <div style="display:flex;gap:0.4rem;justify-content:center;">
                <p-button icon="pi pi-pencil" styleClass="p-button-sm p-button-text" pTooltip="Edit" (onClick)="openEdit(o)"></p-button>
                <p-button icon="pi pi-trash" styleClass="p-button-sm p-button-text p-button-danger" pTooltip="Delete" (onClick)="confirmDelete(o)"></p-button>
              </div>
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="7" style="text-align:center;padding:3rem;color:var(--color-text-dim);">No orders found</td></tr>
        </ng-template>
      </p-table>
    </div>

    <p-dialog [header]="editMode ? 'Edit Order' : 'New Order'" [(visible)]="dialogVisible"
              [modal]="true" [style]="{width:'520px'}">
      <div style="display:flex;flex-direction:column;gap:1rem;">
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
          <div class="field">
            <label class="field-label">Order Reference *</label>
            <input pInputText [(ngModel)]="form.orderReference" placeholder="ORD-2025-001" style="width:100%;margin-top:0.35rem;"/>
          </div>
          <div class="field">
            <label class="field-label">Status *</label>
            <p-dropdown [(ngModel)]="form.status" [options]="statusOptions"
                        placeholder="Select status" [style]="{width:'100%',marginTop:'0.35rem'}" appendTo="body">
            </p-dropdown>
          </div>
        </div>
        <div class="field">
          <label class="field-label">Trek Name *</label>
          <input pInputText [(ngModel)]="form.trekName" placeholder="e.g. Everest Base Camp Trek" style="width:100%;margin-top:0.35rem;"/>
        </div>
        <div class="field">
          <label class="field-label">Trek Group *</label>
          <p-dropdown [(ngModel)]="form.trekGroupId" [options]="trekGroups"
                      optionLabel="groupName" optionValue="id"
                      placeholder="Select trek group" [style]="{width:'100%'}" appendTo="body">
          </p-dropdown>
        </div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
          <div class="field">
            <label class="field-label">Start Date *</label>
            <p-calendar [(ngModel)]="form.startDate" dateFormat="yy-mm-dd"
                        [style]="{width:'100%',marginTop:'0.35rem'}" appendTo="body">
            </p-calendar>
          </div>
          <div class="field">
            <label class="field-label">End Date *</label>
            <p-calendar [(ngModel)]="form.endDate" dateFormat="yy-mm-dd"
                        [style]="{width:'100%',marginTop:'0.35rem'}" appendTo="body">
            </p-calendar>
          </div>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="dialogVisible=false"></p-button>
        <p-button [label]="editMode ? 'Update' : 'Create'" icon="pi pi-save" [loading]="saving"
                  [disabled]="!form.orderReference.trim() || !form.trekName.trim() || !form.trekGroupId || !form.status"
                  (onClick)="save()"></p-button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .field-label{font-size:.8rem;font-weight:600;color:var(--color-text-dim);}
    .order-status{font-size:10px;font-weight:700;letter-spacing:.06em;padding:3px 10px;border-radius:20px;}
    .os-planned   {background:rgba(148,163,184,.15);color:#94a3b8;border:1px solid rgba(148,163,184,.3);}
    .os-active    {background:rgba(16,185,129,.15); color:var(--color-success);border:1px solid rgba(16,185,129,.3);}
    .os-completed {background:rgba(99,102,241,.15); color:var(--color-info);border:1px solid rgba(99,102,241,.3);}
    .os-cancelled {background:rgba(239,68,68,.15);  color:var(--color-danger);border:1px solid rgba(239,68,68,.3);}
  `]
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  trekGroups: TrekGroup[] = [];
  loading = false;
  filter = '';
  dialogVisible = false;
  editMode = false;
  saving = false;
  selectedId?: number;
  statusOptions = ['PLANNED','ACTIVE','COMPLETED','CANCELLED'];
  form: any = { orderReference:'', trekName:'', startDate:null, endDate:null, status:'PLANNED', trekGroupId:null };

  constructor(
    private svc: OrderService,
    private trekGroupSvc: TrekGroupService,
    private messageService: MessageService,
    private confirmService: ConfirmationService
  ) {}

  ngOnInit(): void { this.load(); this.trekGroupSvc.getAll().subscribe(g => this.trekGroups = g); }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({ next: o => { this.orders = o; this.loading = false; }, error: () => this.loading = false });
  }

  openAdd(): void { this.editMode = false; this.form = { orderReference:'', trekName:'', startDate:null, endDate:null, status:'PLANNED', trekGroupId:null }; this.dialogVisible = true; }

  openEdit(o: Order): void {
    this.editMode = true; this.selectedId = o.id;
    this.form = { orderReference: o.orderReference, trekName: o.trekName, startDate: o.startDate, endDate: o.endDate, status: o.status, trekGroupId: o.trekGroupId };
    this.dialogVisible = true;
  }

  private formatDate(d: any): string {
    if (!d) return '';
    if (typeof d === 'string') return d;
    const dt = d as Date;
    return `${dt.getFullYear()}-${String(dt.getMonth()+1).padStart(2,'0')}-${String(dt.getDate()).padStart(2,'0')}`;
  }

  save(): void {
    this.saving = true;
    const payload = { ...this.form, startDate: this.formatDate(this.form.startDate), endDate: this.formatDate(this.form.endDate) };
    const obs = this.editMode ? this.svc.update(this.selectedId!, payload) : this.svc.create(payload);
    obs.subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Saved', detail: `Order ${this.editMode ? 'updated' : 'created'}.` }); this.dialogVisible = false; this.saving = false; this.load(); },
      error: () => this.saving = false
    });
  }

  confirmDelete(o: Order): void {
    this.confirmService.confirm({
      message: `Delete order <strong>${o.orderReference}</strong>?`,
      header: 'Confirm Delete', icon: 'pi pi-trash',
      accept: () => this.svc.delete(o.id).subscribe(() => { this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Order removed.' }); this.load(); })
    });
  }
}
