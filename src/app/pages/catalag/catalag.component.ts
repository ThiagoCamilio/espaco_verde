import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, NgFor } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { ProductCardComponent } from '../../components/produto-card/produto-card.component';

@Component({
  selector: 'app-catalog',
  standalone: true,
  templateUrl: './catalag.component.html',
  styleUrls: ['./catalag.component.css'] ,
  imports: [CurrencyPipe, NgFor, RouterLink, ProductCardComponent]
})
export class CatalogComponent implements OnInit{
  products : Product[] = [];

  activeFilter: string = 'Todas';

  constructor(private router: Router, private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(){
    this.productService.listAll().subscribe({
      next: (data) =>{
        this.products = data;
      },
      error(err) {
        console.log("Houve um erro", err)
      },
    })
  }

  filterProducts(category: string) {
    this.activeFilter = category;
  }

  addToCart(product: any) {
    alert(`${product.name} adicionado ao carrinho!`);

  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]); // Navega para a rota
  }
}
