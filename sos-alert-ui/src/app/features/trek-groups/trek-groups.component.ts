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
import { TrekGroupService } from '../../core/services/trek-group.service';
import { TrekGroup } from '../../core/models/models';

@Component({
  selector: 'app-trek-groups',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, DialogModule, InputTextModule, TagModule, TooltipModule],
  template: `
    <div class="page-header">
      <div style="display:flex;align-items:center;justify-content:space-between;">
        <div>
          <h1>👥 Trek Groups</h1>
          <p>Manage trekking groups assigned to expeditions</p>
        </div>
        <p-button label="Add Trek Group" icon="pi pi-plus" (onClick)="openAdd()"></p-button>
      </div>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
      <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border-subtle);">
        <span class="p-input-icon-left">
          <i class="pi pi-search"></i>
          <input pInputText [(ngModel)]="filter" placeholder="Search groups..." (input)="dt.filterGlobal(filter,'contains')"/>
        </span>
      </div>
      <p-table #dt [value]="groups" [loading]="loading" [paginator]="true" [rows]="15"
               [globalFilterFields]="['groupCode','groupName']"
               [rowHover]="true" styleClass="p-datatable-sm"
               [showCurrentPageReport]="true" currentPageReportTemplate="{first}-{last} of {totalRecords}">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px">#</th>
            <th pSortableColumn="groupCode">Group Code <p-sortIcon field="groupCode"/></th>
            <th pSortableColumn="groupName">Group Name <p-sortIcon field="groupName"/></th>
            <th>Status</th>
            <th pSortableColumn="createdAt">Created <p-sortIcon field="createdAt"/></th>
            <th style="width:130px;text-align:center;">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-g>
          <tr>
            <td><span style="font-size:11px;color:var(--color-muted);font-family:monospace;">#{{ g.id }}</span></td>
            <td><strong style="font-family:monospace;">{{ g.groupCode }}</strong></td>
            <td>{{ g.groupName }}</td>
            <td><p-tag [value]="g.active ? 'Active' : 'Inactive'" [severity]="g.active ? 'success' : 'danger'"></p-tag></td>
            <td style="font-size:0.8rem;color:var(--color-text-dim);">{{ g.createdAt | date:'MMM d, y' }}</td>
            <td>
              <div style="display:flex;gap:0.4rem;justify-content:center;">
                <p-button icon="pi pi-pencil" styleClass="p-button-sm p-button-text" pTooltip="Edit" (onClick)="openEdit(g)"></p-button>
                <p-button icon="pi pi-trash" styleClass="p-button-sm p-button-text p-button-danger" pTooltip="Delete" (onClick)="confirmDelete(g)"></p-button>
              </div>
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="6" style="text-align:center;padding:3rem;color:var(--color-text-dim);">No trek groups found</td></tr>
        </ng-template>
      </p-table>
    </div>

    <p-dialog [header]="editMode ? 'Edit Trek Group' : 'Add Trek Group'" [(visible)]="dialogVisible"
              [modal]="true" [style]="{width:'420px'}">
      <div style="display:flex;flex-direction:column;gap:1rem;">
        <div class="field">
          <label class="field-label">Group Code *</label>
          <input pInputText [(ngModel)]="form.groupCode" placeholder="e.g. TG-EBC-2025" style="width:100%;margin-top:0.35rem;"/>
        </div>
        <div class="field">
          <label class="field-label">Group Name *</label>
          <input pInputText [(ngModel)]="form.groupName" placeholder="e.g. Everest Base Camp Team" style="width:100%;margin-top:0.35rem;"/>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" styleClass="p-button-text" (onClick)="dialogVisible=false"></p-button>
        <p-button [label]="editMode ? 'Update' : 'Create'" icon="pi pi-save" [loading]="saving"
                  [disabled]="!form.groupCode.trim() || !form.groupName.trim()" (onClick)="save()"></p-button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`.field-label{font-size:.8rem;font-weight:600;color:var(--color-text-dim);}`]
})
export class TrekGroupsComponent implements OnInit {
  groups: TrekGroup[] = [];
  loading = false;
  filter = '';
  dialogVisible = false;
  editMode = false;
  saving = false;
  selectedId?: number;
  form = { groupCode: '', groupName: '' };

  constructor(
    private svc: TrekGroupService,
    private messageService: MessageService,
    private confirmService: ConfirmationService
  ) {}

  ngOnInit(): void { this.load(); }
  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({ next: g => { this.groups = g; this.loading = false; }, error: () => this.loading = false });
  }
  openAdd(): void { this.editMode = false; this.form = { groupCode: '', groupName: '' }; this.dialogVisible = true; }
  openEdit(g: TrekGroup): void {
    this.editMode = true; this.selectedId = g.id;
    this.form = { groupCode: g.groupCode, groupName: g.groupName };
    this.dialogVisible = true;
  }
  save(): void {
    this.saving = true;
    const obs = this.editMode ? this.svc.update(this.selectedId!, this.form) : this.svc.create(this.form);
    obs.subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Saved', detail: `Trek group ${this.editMode ? 'updated' : 'created'}.` }); this.dialogVisible = false; this.saving = false; this.load(); },
      error: () => this.saving = false
    });
  }
  confirmDelete(g: TrekGroup): void {
    this.confirmService.confirm({
      message: `Delete trek group <strong>${g.groupName}</strong>?`,
      header: 'Confirm Delete', icon: 'pi pi-trash',
      accept: () => this.svc.delete(g.id).subscribe(() => { this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Trek group removed.' }); this.load(); })
    });
  }
}
