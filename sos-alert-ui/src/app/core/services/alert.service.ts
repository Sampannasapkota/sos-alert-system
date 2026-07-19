import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Alert, AlertRequest, AlertClaimRequest, ApiResponse, PaginatedResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AlertService {
  private base = `${environment.apiUrl}/alerts`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Alert[]> {
    return this.http.get<ApiResponse<PaginatedResponse<Alert>>>(this.base).pipe(map(r => r.data.content));
  }

  getById(id: number): Observable<Alert> {
    return this.http.get<ApiResponse<Alert>>(`${this.base}/${id}`).pipe(map(r => r.data));
  }

  create(payload: AlertRequest): Observable<Alert> {
    return this.http.post<ApiResponse<Alert>>(this.base, payload).pipe(map(r => r.data));
  }

  claim(id: number, payload: AlertClaimRequest): Observable<Alert> {
    return this.http.post<ApiResponse<Alert>>(`${this.base}/${id}/claim`, payload).pipe(map(r => r.data));
  }

  resolve(id: number): Observable<Alert> {
    return this.http.post<ApiResponse<Alert>>(`${this.base}/${id}/resolve`, {}).pipe(map(r => r.data));
  }
}
