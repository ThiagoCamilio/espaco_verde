import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { ActivatedRoute, Router } from '@angular/router';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent implements OnInit {

  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;
  product!: Product
  productId: string | null = null;
  quantity: number = 1;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private cartService: CartService,
    private authService: AuthService,
    private router: Router
  ) {

  }

  ngOnInit(): void {
    this.productId = this.route.snapshot.paramMap.get('id');
    this.productService.listById(this.productId!).subscribe({
      next: (data) => {
        this.product = data;
      },
      error(err) {
        console.log("ERRO")
      }
    })
  }

  modifyQuantity(value: number) {
    if (this.quantity + value >= 1) {
      this.quantity += value;
    }
  }

  addToCart() {

    if (!this.authService.hasToken()) {
      alert('Você precisa estar logado para adicionar itens ao carrinho!');
      this.router.navigate(['/login']);
      return;
    }

    const item: CartItem = {
      id: this.product.id!,
      productDTO: this.product,
      quantity: this.quantity,
      sellPrice: this.product.preco,
    };

    this.cartService.addItem(item).subscribe({
      next: (res) => {
        console.log("UUHUL")
        console.log(res.productCarts[0].quantity)
      },
      error(err) {
        console.log(err)
      },
    });

  }


}
