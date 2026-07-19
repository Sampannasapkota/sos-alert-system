import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MessageService } from 'primeng/api';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private messageService: MessageService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let summary = 'Request Failed';
        let detail = 'An unexpected error occurred.';

        if (error.status === 0) {
          summary = 'Network Error';
          detail = 'Cannot reach the server. Make sure the backend is running on port 8080.';
        } else if (error.error?.message) {
          detail = error.error.message;
        } else if (error.message) {
          detail = error.message;
        }

        switch (error.status) {
          case 400: summary = 'Validation Error'; break;
          case 404: summary = 'Not Found'; break;
          case 409: summary = 'Conflict'; break;
          case 500: summary = 'Server Error'; break;
        }

        this.messageService.add({
          severity: 'error',
          summary,
          detail,
          life: 5000
        });

        return throwError(() => error);
      })
    );
  }
}
