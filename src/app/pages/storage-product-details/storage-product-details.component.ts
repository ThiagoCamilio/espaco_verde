import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { BannerService } from '../../services/banner.service';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environment';
import { FormsModule } from '@angular/forms';
import { DeleteModalComponent } from '../../components/delete-modal/delete-modal.component';
import { PricingCategory } from '../../models/pricing-category';
import { ToastrService } from 'ngx-toastr';
import { PricingService } from '../../services/pricing.service';

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
export class StorageProductDetailsComponent implements OnInit {

  @Input() pricingCategories!: PricingCategory[];
  product!: any;
  editedProduct!: Product;
  productId: string | null = null;
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;
  editToggle: boolean = false;
  modalToggle: boolean = false;
  imagePreview: string | ArrayBuffer | null = null;
  selectedFile!: File


  constructor(
    private route: ActivatedRoute,
    private productService: ProductService, 
    private bannerService: BannerService, 
    private toastr: ToastrService, 
    private pricingService: PricingService
  ) { }

  ngOnInit(): void {
    setTimeout(() => {
      this.bannerService.showBanner.next(false);
    })
    this.pricingService.getCategories().subscribe(
      res => this.pricingCategories = res
    );
    this.productId = this.route.snapshot.paramMap.get('id');
    this.productService.listById(this.productId!).subscribe({
      next: (data) => {
        this.product = data;
        this.editedProduct = JSON.parse(JSON.stringify(this.product))
      },
      error(err) {
        console.log("ERRO")
      }
    })

    this.route.queryParams.subscribe(params => {
      this.editToggle = params['edit'] === 'true';
    });
  }

  ngOnDestroy(): void {
    this.bannerService.showBanner.next(true);
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onEditToggle(): void {
    this.editToggle = !this.editToggle;
    this.editedProduct = JSON.parse(JSON.stringify(this.product))
    this.imagePreview = null;
  }

  onModalToggle(): void {
    this.modalToggle = !this.modalToggle;
  }

  saveEdit(): void {
    this.productService.save(this.editedProduct, this.selectedFile).subscribe({
      next: (res) => {
        this.product = res
        alert('Produto salvo!');
        this.onEditToggle();
      },
      error: (err) => alert('Erro ao salvar product.' + err)
    });
  }

  onCategoryChange(newId: number): void {
    console.log(this.product.pricingCategory)
    if (this.product.pricingCategory) {
      this.product.pricingCategory.id = newId
    } else {
      this.product.pricingCategory = { id: newId } as any;
    }

    this.productService.updatePricingCategory(this.product.id!, newId).subscribe({
      next: () => {
        this.toastr.success("Categoria de precificação atualizada!", "Sucesso")
      },
      error: (err) => this.toastr.error("Um erro aconteceu ao atualizar a categoria de precificação", "Erro")
    })
  }
  onToggleSync(): void {
    if (this.product.useSuggestedPrice) {
      if (this.product.suggestedPrice && this.product.suggestedPrice > 0) {
        this.product.preco = this.product.suggestedPrice;
      } else {
        this.toastr.error("Nenhum preço sugerido cadastrado para esse produto", "Erro")
        this.product.useSuggestedPrice = !this.product.useSuggestedPrice;
        return;
      }
    }
    this.productService.updateSyncStatus(this.product.id!, this.product.useSuggestedPrice).subscribe({
      next: () => {
        this.toastr.success("Preço sincronizado com o preço de venda!", "Sucesso");
      },
      error: (err) => {
        this.product.useSuggestedPrice = !this.product.useSuggestedPrice;
        this.toastr.error("Erro ao sincornizar o preço do produto", "Erro")
      },
    })

  }


}
