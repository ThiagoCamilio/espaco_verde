import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {

  items$: Observable<CartItem[]>; 
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;
  

  constructor(private cartService: CartService){
    this.items$ = this.cartService.items$;
  }

  addItem(item:CartItem ){
    this.cartService.addItem(item);
  }

  removeItem(productId:string){
    this.cartService.removeItem(productId);
  }

  cleanCart(){
    this.cartService.clean()
  }

  get totalPrice() {
    return this.cartService.totalPrice;
  }

  get totalItems() {
    return this.cartService.totalItems;
  }

}
