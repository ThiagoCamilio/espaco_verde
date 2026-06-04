import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { environment } from '../../../environment';
import { CommonModule } from '@angular/common';
import { BaseProductCardComponent } from '../base-product-card/base-product-card.component';
import { CartService } from '../../services/cart.service';
import { CartItem } from '../../models/cart-item';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [
    CommonModule,
    BaseProductCardComponent,
    RouterLink
  ],
  templateUrl: './product-card.component.html',
  styleUrl: './product-card.component.css'
})
export class ProductCardComponent {

  @Input() products!: Product[];
  @Input() product!: Product;

  constructor (private cartService: CartService, private authService:AuthService, private router:Router){ }

  addToCart(event: Event){
    event.stopPropagation();
    if(!this.authService.hasToken()){
      alert('Você precisa estar logado para adicionar itens ao carrinho!');
      this.router.navigate(['/login']);
      return;
    }
    
    const item: CartItem = {
      id : this.product.id!,
      productDTO: this.product,
      quantity: 1,
      sellPrice: this.product.preco,
    };

    this.cartService.addItem(item).subscribe({
      next:(res) =>{
        console.log("UUHUL")
        console.log(res)
      },
      error(err) {
        console.log(err)
      },
    });
  }

}
