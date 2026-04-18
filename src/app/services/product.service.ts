import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { environment } from '../../environment';

@Injectable({providedIn: 'root'})
export class ProductService {

  private readonly URL = `${environment.apiUrl}/produtos`
  constructor(private http: HttpClient) {}

  save(produto:any, imagem:File){

    const formData = new FormData();

    const blob = new Blob([JSON.stringify(produto)], {type: 'application/json'});

    formData.append('produto', blob);
    formData.append('imagem', imagem);

    return this.http.post(`${this.URL}/register `, formData);

  }

  listAll(): Observable<Product[]>{

    return this.http.get<Product[]>(this.URL);

  }

  listById(id:string): Observable<Product>{
    return this.http.get<Product>(`${this.URL}/produto/`+id)
  }
}
