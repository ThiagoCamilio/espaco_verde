import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';
import { Router, RouterLink } from '@angular/router';
import { OrderSummaryComponent } from '../../components/order-summary/order-summary.component';
import { Cart } from '../../models/cart';
import { ToastrService } from 'ngx-toastr';

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
export class CartComponent implements OnInit{

  cart!: Cart;
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;


  constructor(
    private cartService: CartService,
    private router: Router,
    private toastrService: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void{
    this.cartService.getMyCart().subscribe({
      next:(res) =>{
        this.cart = res
      },
      error:(err)=>{
        console.log(err)
      }
    })
  }

  goToDelivery(): void {
    this.router.navigate(['user/checkout/delivery']);
  }

  addItem(produtcId: string) {
    this.cartService.addItem(produtcId, 1).subscribe({
      next: (res) => {
        this.cart = res;
        this.toastrService.success("Produto adicionado ao carrinho!", "");
      },
      error:(err) =>{
        this.toastrService.error("Produto não adicionado", "Algo deu errado");
      },
    });
  }

  removeItem(productId: string) {

    this.cartService.removeItem(productId).subscribe({
      next: (res) => {
        this.cart = res;
        this.toastrService.warning("Produto removido do carrinho!", "");
      },
      error:(err) => {
        this.toastrService.error("Produto não removido", "Algo deu errado");
      },
    });
  }

  cleanCart() {
    this.cartService.clean().subscribe({
      next:(res) =>{
        this.toastrService.warning("Carrinho limpo!", "");
        this.cart = res;
      },
      error(err){
        console.log(err)
      }
    })
  }

  get totalPrice(){
    if (!this.cart || !this.cart.productCartDTOs) return 0;
    return this.cart.price;
  }

  get totalItems() {
    if (!this.cart || !this.cart.productCartDTOs) return 0;
    return this.cart.productCartDTOs.reduce((acc: number, item: any) => acc + item.quantity, 0);
  }

}
