import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Device, DeviceRequest, ApiResponse, PaginatedResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DeviceService {
  private base = `${environment.apiUrl}/devices`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Device[]> {
    return this.http.get<ApiResponse<PaginatedResponse<Device>>>(this.base).pipe(map(r => r.data.content));
  }

  getById(id: number): Observable<Device> {
    return this.http.get<ApiResponse<Device>>(`${this.base}/${id}`).pipe(map(r => r.data));
  }

  create(payload: DeviceRequest): Observable<Device> {
    return this.http.post<ApiResponse<Device>>(this.base, payload).pipe(map(r => r.data));
  }

  update(id: number, payload: DeviceRequest): Observable<Device> {
    return this.http.put<ApiResponse<Device>>(`${this.base}/${id}`, payload).pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
