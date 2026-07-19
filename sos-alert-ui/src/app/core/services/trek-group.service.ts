import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { TrekGroup, TrekGroupRequest, ApiResponse, PaginatedResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class TrekGroupService {
  private base = `${environment.apiUrl}/trek-groups`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<TrekGroup[]> {
    return this.http.get<ApiResponse<PaginatedResponse<TrekGroup>>>(this.base).pipe(map(r => r.data.content));
  }

  create(payload: TrekGroupRequest): Observable<TrekGroup> {
    return this.http.post<ApiResponse<TrekGroup>>(this.base, payload).pipe(map(r => r.data));
  }

  update(id: number, payload: TrekGroupRequest): Observable<TrekGroup> {
    return this.http.put<ApiResponse<TrekGroup>>(`${this.base}/${id}`, payload).pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
