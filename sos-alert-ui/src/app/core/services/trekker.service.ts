import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Trekker, TrekkerRequest, ApiResponse, PaginatedResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class TrekkerService {
  private base = `${environment.apiUrl}/trekkers`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Trekker[]> {
    return this.http.get<ApiResponse<PaginatedResponse<Trekker>>>(this.base).pipe(map(r => r.data.content));
  }

  create(payload: TrekkerRequest): Observable<Trekker> {
    return this.http.post<ApiResponse<Trekker>>(this.base, payload).pipe(map(r => r.data));
  }

  update(id: number, payload: TrekkerRequest): Observable<Trekker> {
    return this.http.put<ApiResponse<Trekker>>(`${this.base}/${id}`, payload).pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
