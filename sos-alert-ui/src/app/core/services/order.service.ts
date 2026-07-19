import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Order, OrderRequest, ApiResponse, PaginatedResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private base = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Order[]> {
    return this.http.get<ApiResponse<PaginatedResponse<Order>>>(this.base).pipe(map(r => r.data.content));
  }

  create(payload: OrderRequest): Observable<Order> {
    return this.http.post<ApiResponse<Order>>(this.base, payload).pipe(map(r => r.data));
  }

  update(id: number, payload: OrderRequest): Observable<Order> {
    return this.http.put<ApiResponse<Order>>(`${this.base}/${id}`, payload).pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
