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
  selector: 'app-produto-card',
  standalone: true,
  imports: [
    CommonModule,
    BaseProductCardComponent,
    RouterLink
  ],
  templateUrl: './produto-card.component.html',
  styleUrl: './produto-card.component.css'
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
      produtcId : this.product.id!,
      name: this.product.nome,
      quantity: 1,
      price: this.product.preco,
      imageUrl : this.product.imagem
    };

    this.cartService.addItem(item);
    console.log('Produto adicionado:', item.name);
  }

}
