import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private readonly URL = `${environment.apiUrl}/admin/reports`

  constructor(private http: HttpClient) { }

  generateReport(filters: any): Observable<any> {
    return this.http.post<any>(`${this.URL}/generate`, filters);
  }

  getReportHistory(): Observable<any[]> {
    return this.http.get<any[]>(this.URL);
  }

  downloadPdf(reportId: number): Observable<Blob> {
    return this.http.get(`${this.URL}/${reportId}/pdf`, {
      responseType: 'blob'
    });
  }
}
