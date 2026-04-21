import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { BannerService } from '../../services/banner.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';
import { FormsModule } from '@angular/forms';
import { DeleteModalComponent } from '../../components/delete-modal/delete-modal.component';

@Component({
  selector: 'app-storage-product-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DeleteModalComponent
  ],
  templateUrl: './storage-product-details.component.html',
  styleUrl: './storage-product-details.component.css'
})
export class StorageProductDetailsComponent implements OnInit{

  product!: any;
  editedProduct!: Product;
  productId: string | null = null;
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;
  editToggle: boolean = false;
  modalToggle: boolean = false;
  imagePreview: string | ArrayBuffer | null = null;
  selectedFile!: File
  

  constructor(private route: ActivatedRoute, private productService : ProductService, private bannerService: BannerService){}

  ngOnInit(): void {
    setTimeout(()=>{
      this.bannerService.showBanner.next(false);
    })
    this.productId = this.route.snapshot.paramMap.get('id');
    this.productService.listById(this.productId!).subscribe({
      next: (data) =>{
        this.product = data;
        this.editedProduct = JSON.parse(JSON.stringify(this.product))
      },
      error(err){
        console.log("ERRO")
      }
    })

    this.route.queryParams.subscribe( params=>{
      this.editToggle = params['edit'] === 'true';
    });
  }

  ngOnDestroy():void{
    this.bannerService.showBanner.next(true);
  }

  onFileSelected(event:any){
    const file = event.target.files[0];
    if(file){
      this.selectedFile = file
      const reader = new FileReader();
      reader.onload = ()=>{
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onEditToggle():void{
    this.editToggle = !this.editToggle;
    this.editedProduct = JSON.parse(JSON.stringify(this.product))
    this.imagePreview = null;
  }

  onModalToggle():void{
    this.modalToggle = !this.modalToggle;
  }

 saveEdit():void{
    this.productService.save(this.editedProduct, this.selectedFile).subscribe({
      next: (res) => {
      this.product = res  
      alert('Produto salvo!');
    },
    error: (err) => alert('Erro ao salvar produto.')
    });
  }

}
