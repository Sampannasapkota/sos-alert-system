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
import { MessageService, ConfirmationService } from 'primeng/api';
import { TrekkerService } from '../../core/services/trekker.service';
import { TrekGroupService } from '../../core/services/trek-group.service';
import { Trekker, TrekGroup } from '../../core/models/models';

@Component({
  selector: 'app-trekkers',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, DialogModule, InputTextModule, TagModule, TooltipModule, DropdownModule],
  template: `
    <div class="page-header">
      <div style="display:flex;align-items:center;justify-content:space-between;">
        <div>
          <h1>🧗 Trekkers</h1>
          <p>Manage individual trekkers registered in trek groups</p>
        </div>
        <p-button label="Add Trekker" icon="pi pi-plus" (onClick)="openAdd()"></p-button>
      </div>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
      <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border-subtle);">
        <span class="p-input-icon-left">
          <i class="pi pi-search"></i>
          <input pInputText [(ngModel)]="filter" placeholder="Search trekkers..." (input)="dt.filterGlobal(filter,'contains')"/>
        </span>
      </div>
      <p-table #dt [value]="trekkers" [loading]="loading" [paginator]="true" [rows]="15"
               [globalFilterFields]="['fullName','nationality','trekGroupName','phoneNumber']"
               [rowHover]="true" styleClass="p-datatable-sm"
               [showCurrentPageReport]="true" currentPageReportTemplate="{first}-{last} of {totalRecords}">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px">#</th>
            <th pSortableColumn="fullName">Full Name <p-sortIcon field="fullName"/></th>
            <th>Nationality</th>
            <th>Phone</th>
            <th>Emergency Contact</th>
            <th pSortableColumn="trekGroupName">Trek Group <p-sortIcon field="trekGroupName"/></th>
            <th>Status</th>
            <th style="width:130px;text-align:center;">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-t>
          <tr>
            <td><span style="font-size:11px;color:var(--color-muted);font-family:monospace;">#{{ t.id }}</span></td>
            <td><strong>{{ t.fullName }}</strong></td>
            <td>{{ t.nationality || '—' }}</td>
            <td style="font-size:0.8rem;">{{ t.phoneNumber || '—' }}</td>
            <td style="font-size:0.8rem;">{{ t.emergencyContact || '—' }}</td>
            <td>
              <div style="display:flex;flex-direction:column;">
                <span style="font-weight:600;font-size:0.85rem;">{{ t.trekGroupName }}</span>
                <span style="font-size:0.72rem;color:var(--color-text-dim);font-family:monospace;">{{ t.trekGroupCode }}</span>
              </div>
            </td>
            <td><p-tag [value]="t.active ? 'Active' : 'Inactive'" [severity]="t.active ? 'success' : 'danger'"></p-tag></td>
            <td>
              <div style="display:flex;gap:0.4rem;justify-content:center;">
                <p-button icon="pi pi-pencil" styleClass="p-button-sm p-button-text" pTooltip="Edit" (onClick)="openEdit(t)"></p-button>
                <p-button icon="pi pi-trash" styleClass="p-button-sm p-button-text p-button-danger" pTooltip="Delete" (onClick)="confirmDelete(t)"></p-button>
              </div>
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="8" style="text-align:center;padding:3rem;color:var(--color-text-dim);">No trekkers found</td></tr>
        </ng-template>
      </p-table>
    </div>

    <p-dialog [header]="editMode ? 'Edit Trekker' : 'Add Trekker'" [(visible)]="dialogVisible"
              [modal]="true" [style]="{width:'480px'}">
      <div style="display:flex;flex-direction:column;gap:1rem;">
        <div class="field">
          <label class="field-label">Full Name *</label>
          <input pInputText [(ngModel)]="form.fullName" placeholder="e.g. John Doe" style="width:100%;margin-top:0.35rem;"/>
        </div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
          <div class="field">
            <label class="field-label">Nationality</label>
            <input pInputText [(ngModel)]="form.nationality" placeholder="e.g. Nepali" style="width:100%;margin-top:0.35rem;"/>
          </div>
          <div class="field">
            <label class="field-label">Phone Number</label>
            <input pInputText [(ngModel)]="form.phoneNumber" placeholder="+977-..." style="width:100%;margin-top:0.35rem;"/>
          </div>
        </div>
        <div class="field">
          <label class="field-label">Emergency Contact</label>
          <input pInputText [(ngModel)]="form.emergencyContact" placeholder="+1-..." style="width:100%;margin-top:0.35rem;"/>
        </div>
        <div class="field">
          <label class="field-label">Trek Group *</label>
          <p-dropdown [(ngModel)]="form.trekGroupId" [options]="trekGroups"
                      optionLabel="groupName" optionValue="id"
                      placeholder="Select trek group" [style]="{width:'100%'}" appendTo="body">
          </p-dropdown>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="dialogVisible=false"></p-button>
        <p-button [label]="editMode ? 'Update' : 'Create'" icon="pi pi-save" [loading]="saving"
                  [disabled]="!form.fullName.trim() || !form.trekGroupId" (onClick)="save()"></p-button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`.field-label{font-size:.8rem;font-weight:600;color:var(--color-text-dim);}`]
})
export class TrekkersComponent implements OnInit {
  trekkers: Trekker[] = [];
  trekGroups: TrekGroup[] = [];
  loading = false;
  filter = '';
  dialogVisible = false;
  editMode = false;
  saving = false;
  selectedId?: number;
  form: any = { fullName: '', nationality: '', phoneNumber: '', emergencyContact: '', trekGroupId: null };

  constructor(
    private svc: TrekkerService,
    private trekGroupSvc: TrekGroupService,
    private messageService: MessageService,
    private confirmService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.load();
    this.trekGroupSvc.getAll().subscribe(g => this.trekGroups = g);
  }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({ next: t => { this.trekkers = t; this.loading = false; }, error: () => this.loading = false });
  }

  openAdd(): void { this.editMode = false; this.form = { fullName: '', nationality: '', phoneNumber: '', emergencyContact: '', trekGroupId: null }; this.dialogVisible = true; }

  openEdit(t: Trekker): void {
    this.editMode = true; this.selectedId = t.id;
    this.form = { fullName: t.fullName, nationality: t.nationality, phoneNumber: t.phoneNumber, emergencyContact: t.emergencyContact, trekGroupId: t.trekGroupId };
    this.dialogVisible = true;
  }

  save(): void {
    this.saving = true;
    const obs = this.editMode ? this.svc.update(this.selectedId!, this.form) : this.svc.create(this.form);
    obs.subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Saved', detail: `Trekker ${this.editMode ? 'updated' : 'created'}.` }); this.dialogVisible = false; this.saving = false; this.load(); },
      error: () => this.saving = false
    });
  }

  confirmDelete(t: Trekker): void {
    this.confirmService.confirm({
      message: `Delete trekker <strong>${t.fullName}</strong>?`,
      header: 'Confirm Delete', icon: 'pi pi-trash',
      accept: () => this.svc.delete(t.id).subscribe(() => { this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Trekker removed.' }); this.load(); })
    });
  }
}
