import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../components/produto-card/produto-card.component';
import { NgFor } from '@angular/common';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { BannerComponent } from '../../components/banner/banner.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    SidebarComponent, 
    RouterLink, 
    ProductCardComponent, 
    NgFor,
    BannerComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{

  constructor(private productService: ProductService){}

  products : Product[] = []

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(){
    this.productService.listAll().subscribe({
      next:(data) =>{
        this.products = data;
      },
      error(err){
        console.log("Houve um erro", err)
      }
    })
  }

}
