// ─── Alert ───────────────────────────────────────────────────────────────────
export type AlertStatus = 'RECEIVED' | 'CLAIMED' | 'ESCALATED' | 'RESOLVED';

export interface AlertRequest {
  deviceId: number;
  latitude: number;
  longitude: number;
  timestamp: string; // ISO LocalDateTime
}

export interface AlertClaimRequest {
  coordinatorName: string;
}

export interface Alert {
  id: number;
  version: number;
  deviceId: number;
  deviceCode: string;
  deviceDisplayName: string;
  orderId: number;
  orderReference: string;
  trekName: string;
  trekGroupId: number;
  trekGroupCode: string;
  trekGroupName: string;
  latitude: number;
  longitude: number;
  alertTimestamp: string;
  status: AlertStatus;
  claimedBy: string | null;
  claimedAt: string | null;
  escalatedAt: string | null;
  resolvedAt: string | null;
  createdAt: string;
  modifiedAt: string;
}

// ─── Device ───────────────────────────────────────────────────────────────────
export interface DeviceRequest {
  deviceCode: string;
  displayName?: string;
}

export interface Device {
  id: number;
  version: number;
  deviceId: string;
  displayName: string;
  active: boolean;
  createdAt: string;
  modifiedAt: string;
}

// ─── TrekGroup ────────────────────────────────────────────────────────────────
export interface TrekGroupRequest {
  groupCode: string;
  groupName: string;
}

export interface TrekGroup {
  id: number;
  version: number;
  groupCode: string;
  groupName: string;
  active: boolean;
  createdAt: string;
  modifiedAt: string;
}

// ─── Trekker ──────────────────────────────────────────────────────────────────
export interface TrekkerRequest {
  fullName: string;
  phoneNumber?: string;
  nationality?: string;
  emergencyContact?: string;
  trekGroupId: number;
}

export interface Trekker {
  id: number;
  version: number;
  fullName: string;
  phoneNumber: string;
  nationality: string;
  emergencyContact: string;
  active: boolean;
  trekGroupId: number;
  trekGroupCode: string;
  trekGroupName: string;
  createdAt: string;
  modifiedAt: string;
}

// ─── Order ────────────────────────────────────────────────────────────────────
export type OrderStatus = 'PLANNED' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED';

export interface OrderRequest {
  orderReference: string;
  trekName: string;
  startDate: string; // LocalDate: YYYY-MM-DD
  endDate: string;
  status: OrderStatus;
  trekGroupId: number;
}

export interface Order {
  id: number;
  version: number;
  orderReference: string;
  trekName: string;
  startDate: string;
  endDate: string;
  status: OrderStatus;
  active: boolean;
  trekGroupId: number;
  trekGroupCode: string;
  trekGroupName: string;
  createdAt: string;
  modifiedAt: string;
}

// ─── DeviceAssignment ─────────────────────────────────────────────────────────
export interface DeviceAssignmentRequest {
  deviceId: number;
  orderId: number;
  assignedFrom: string; // ISO LocalDateTime
  assignedUntil?: string;
}

export interface DeviceAssignment {
  id: number;
  version: number;
  deviceId: number;
  deviceCode: string;
  deviceDisplayName: string;
  orderId: number;
  orderReference: string;
  trekName: string;
  trekGroupId: number;
  trekGroupCode: string;
  trekGroupName: string;
  assignedFrom: string;
  assignedUntil: string | null;
  active: boolean;
  createdAt: string;
  modifiedAt: string;
}

// ─── API Response Wrapper ─────────────────────────────────────────────────────
export interface ApiResponse<T> {
  status: string;
  message: string;
  data: T;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages?: number;
  totalElements?: number;
  size?: number;
  number?: number;
}

