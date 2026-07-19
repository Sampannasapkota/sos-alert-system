import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { DeviceAssignment, DeviceAssignmentRequest, ApiResponse, PaginatedResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DeviceAssignmentService {
  private base = `${environment.apiUrl}/device-assignments`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<DeviceAssignment[]> {
    return this.http.get<ApiResponse<PaginatedResponse<DeviceAssignment>>>(this.base).pipe(map(r => r.data.content));
  }

  create(payload: DeviceAssignmentRequest): Observable<DeviceAssignment> {
    return this.http.post<ApiResponse<DeviceAssignment>>(this.base, payload).pipe(map(r => r.data));
  }

  update(id: number, payload: DeviceAssignmentRequest): Observable<DeviceAssignment> {
    return this.http.put<ApiResponse<DeviceAssignment>>(`${this.base}/${id}`, payload).pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
