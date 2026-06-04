import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { CartItem } from '../models/cart-item';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Cart } from '../models/cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {


  private readonly URL = `${environment.apiUrl}/user/cart`;
  private cartSubject = new BehaviorSubject<Cart | null>(null);
  public cart$ = this.cartSubject.asObservable();

  constructor(private http : HttpClient) { }

  addItem(itemCart : CartItem){
    return this.http.post<any>(`${this.URL}/add`, itemCart).pipe(
      tap(updatedCart => this.cartSubject.next(updatedCart))
    );
  }

  removeItem(produtcId: string){
    return this.http.delete<any>(`${this.URL}/remove/`+produtcId).pipe(
      tap(updatedCart => this.cartSubject.next(updatedCart))
    );

  }

  clean(){
    return this.http.delete<any>(`${this.URL}/clear`).pipe(
      tap(updatedCart => this.cartSubject.next(updatedCart))
    );

  }

  getMyCart():Observable<Cart>{
    return this.http.get<Cart>(`${this.URL}`).pipe(
      tap(updatedCart => this.cartSubject.next(updatedCart))
    )
  }

  private getCart(): CartItem[] {
    const storage = localStorage.getItem('cart');
    return storage ? JSON.parse(storage) : [];
  }

}
