import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CartItem } from '../models/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartItems = new BehaviorSubject<CartItem[]>(this.getCart());
  items$ = this.cartItems.asObservable(); 

  constructor() { }

  addItem(itemCart : CartItem){

    const presentCart = this.cartItems.value;
    const existentItem = presentCart.find(item => item.produtcId === itemCart.produtcId)

    if(existentItem){
      existentItem.quantity += 1;
    }else{
      presentCart.push({...itemCart, quantity:1})
    }

    this.updateState(presentCart);
  }

  removeItem(produtcId: string){
    let presentCart = this.cartItems.value;
    const item = presentCart.find(i => i.produtcId === produtcId);

    if(item && item.quantity > 1){
      item.quantity -= 1;
    }else{
      presentCart = presentCart.filter(i=> i.produtcId !== produtcId);
    }

    this.updateState(presentCart);

  }

  clean(){
    this.updateState([]);
  }

  get totalItems(){
    return this.cartItems.value.reduce((acc, item)=> acc + item.quantity, 0)
  }

  get totalPrice(){
    return this.cartItems.value.reduce((acc, item)=> acc + (item.price * item.quantity), 0)
  }

  private updateState(items: CartItem[]){
    this.cartItems.next(items);
    localStorage.setItem('cart', JSON.stringify(items))
  }

  private getCart(): CartItem[] {
    const storage = localStorage.getItem('cart');
    return storage ? JSON.parse(storage) : [];
  }

}
