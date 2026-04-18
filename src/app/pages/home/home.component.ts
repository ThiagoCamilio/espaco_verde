import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, NgFor } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { ProductCardComponent } from '../../components/produto-card/produto-card.component';
import { BannerComponent } from '../../components/banner/banner.component';
import { LayoutService } from '../../services/layout.service';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'] ,
  imports: [
    CurrencyPipe, 
    NgFor, 
    RouterLink, 
    ProductCardComponent,
    BannerComponent
  ]
})
export class HomeComponent implements OnInit{
  products : Product[] = [];

  activeFilter: string = 'Todas';

  constructor(private router: Router, private productService: ProductService, public layoutService: LayoutService) {}

  ngOnInit(): void {
    this.loadProducts();
    this.layoutService.updateBannerText("Bem-vindo à Espaço Verde!","Descubra nossa coleção de plantas e flores para todos os momentos especiais", "Veja nossas ofertas");
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
    this.router.navigate([`/${route}`]);
  }
}
