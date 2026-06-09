import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { environment } from '../../environment';

@Injectable({providedIn: 'root'})
export class ProductService {

  private readonly URL = `${environment.apiUrl}/produtos`
  constructor(private http: HttpClient) {}

  save(product:any, imagem:File){

    const formData = new FormData();

    const blob = new Blob([JSON.stringify(product)], {type: 'application/json'});

    formData.append('product', blob);
    formData.append('imagem', imagem);

    if(product.id){
      
      return this.http.put(`${this.URL}/update `, formData);
    
    }else{

      return this.http.post(`${this.URL}/register `, formData);
    
    } 

  }

  listAll(): Observable<Product[]>{
    return this.http.get<Product[]>(this.URL);
  }

  listAllActive(): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.URL}/active`);
  }

  toggleStatus(id: string):Observable<void>{
    return this.http.patch<void>(`${this.URL}/${id}/toggle-status`, {})
  }

  listById(id:string): Observable<Product>{
    return this.http.get<Product>(`${this.URL}/`+id)
  }

  delete(id:string){
    return this.http.delete<Product>(`${this.URL}/delete/`+id)
  }

  updateSyncStatus(productId: string, status:boolean):Observable<void>{
    return this.http.patch<void>(`${this.URL}/${productId}/sync-status?status=${status}`, {});
  }

  updatePricingCategory(productId: string, newCategoryId:number) {
    return this.http.patch<void>(`${this.URL}/${productId}/category?categoryId=${newCategoryId}`, {});
  }

}
