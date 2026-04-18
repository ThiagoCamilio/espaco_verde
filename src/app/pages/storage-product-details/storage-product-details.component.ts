import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { BannerService } from '../../services/banner.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';

@Component({
  selector: 'app-storage-product-details',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './storage-product-details.component.html',
  styleUrl: './storage-product-details.component.css'
})
export class StorageProductDetailsComponent implements OnInit{

  product!: Product;
  productId: string | null = null;
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;
  

  constructor(private route: ActivatedRoute, private productService : ProductService, private bannerService: BannerService){}

  ngOnInit(): void {
    this.bannerService.showBanner.next(false);
    this.productId = this.route.snapshot.paramMap.get('id');
    this.productService.listById(this.productId!).subscribe({
      next: (data) =>{
        this.product = data;
      },
      error(err){
        console.log("ERRO")
      }
    })
  }

  ngOnDestroy():void{
    this.bannerService.showBanner.next(true);
  }

}
