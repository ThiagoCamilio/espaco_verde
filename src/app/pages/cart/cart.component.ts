import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';
import { Router, RouterLink } from '@angular/router';
import { OrderSummaryComponent } from '../../components/order-summary/order-summary.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    OrderSummaryComponent
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {

  items$: Observable<CartItem[]>; 
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;


  constructor(private cartService: CartService, private router: Router){
    this.items$ = this.cartService.items$;
  }

  goToDelivery(): void {
    this.router.navigate(['user/checkout/delivery']);
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
