import { Component, OnInit } from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { NgFor, NgIf } from '@angular/common';
import { StorageProductCardComponent } from '../../components/storage-product-card/storage-product-card.component';
import { DeleteModalComponent } from '../../components/delete-modal/delete-modal.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-storage',
  standalone: true,
  imports: [
    StorageProductCardComponent,
    NgFor,
    DeleteModalComponent,
    NgIf,
    RouterLink
  ],
  templateUrl: './storage.component.html',
  styleUrl: './storage.component.css'
})
export class StorageComponent implements OnInit{
  constructor(private layoutService: LayoutService, private productService: ProductService){}

  products : Product[] = [];
  selectedProduct:any;
  modalToggle: boolean = false;

  ngOnInit(): void {
    this.loadProducts();
    setTimeout(() =>{
      this.layoutService.updateBannerText("Bem-vindo ao Estoque!", "Cadastre ou altere seus produtos", "Comece agora");
    });
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

  selectProduct(product: any){
    this.modalToggle = !this.modalToggle;
    this.selectedProduct = product;
  }

}
