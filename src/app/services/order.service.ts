import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private readonly URL = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  createOrder(orderRequest: any): Observable<any> {

    return this.http.post(`${this.URL}/orders`, orderRequest);

  }

  getMyOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.URL}/user/orders`);
  }

  getAllAdminOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.URL}/admin/orders`);
  }

  getOrderById(orderId: string): Observable<any>{
    return this.http.get<any>(`${this.URL}/user/orders/${orderId}?t=${new Date().getTime()}`);
  }

  updateOrderStatus(orderId: number, newStatus: string): Observable<any> {
    return this.http.patch<any[]>(`${this.URL}/admin/orders/${orderId}/status`, { status: newStatus });
  }

}
