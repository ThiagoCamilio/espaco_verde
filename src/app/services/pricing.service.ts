import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PricingConfig } from '../models/pricing-config';
import { PricingCategory } from '../models/pricing-category';
import { PricingMethod } from '../models/pricing-method';

@Injectable({
  providedIn: 'root'
})
export class PricingService {

  private readonly URL = `${environment.apiUrl}/admin/pricing`;

  
  constructor(private http: HttpClient) { }

  getConfig(): Observable<PricingConfig>{
    return this.http.get<PricingConfig>(`${this.URL}/config`);
  }

  getCategories(): Observable<PricingCategory[]>{
    return this.http.get<PricingCategory[]>(`${this.URL}/categories`);
  }

  updateConfig(config : PricingConfig):Observable<void>{
    return this.http.put<void>(`${this.URL}/config`, config);
  }

  updateCategories(margins : Record<number, number>):Observable<void>{
    return this.http.put<void>(`${this.URL}/categories`, margins);
  }

  calculatePrices(method: string):Observable<{message: string}>{
    return this.http.post<{message:string}>(`${this.URL}/calc?method=${method}`,{})
  }

}
