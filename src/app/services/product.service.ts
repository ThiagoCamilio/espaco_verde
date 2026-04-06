import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({providedIn: 'root'})
export class ProductService {

  private readonly URL = 'http://localhost:8080/produtos'
  constructor(private http: HttpClient) {}

  save(produto:any, imagem:File){

    const formData = new FormData();

    const blob = new Blob([JSON.stringify(produto)], {type: 'application/json'});

    formData.append('produto', blob);
    formData.append('imagem', imagem);

    return this.http.post(`${this.URL}/cadastroProduto`, formData);

  }
}
