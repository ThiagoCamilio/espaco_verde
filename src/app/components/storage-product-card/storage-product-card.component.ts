import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product';
import { BaseProductCardComponent } from '../base-product-card/base-product-card.component';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DeleteModalComponent } from '../delete-modal/delete-modal.component';
import { FormsModule } from '@angular/forms';
import { PricingCategory } from '../../models/pricing-category';
import { ProductService } from '../../services/product.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-storage-product-card',
  standalone: true,
  imports: [
    BaseProductCardComponent,
    NgFor,
    RouterLink,
    DeleteModalComponent,
    NgIf,
    CommonModule,
    FormsModule
  ],
  templateUrl: './storage-product-card.component.html',
  styleUrl: './storage-product-card.component.css'
})
export class StorageProductCardComponent {
  @Input() products!: Product[];
  @Input() product!: Product;
  @Input() pricingCategories!: PricingCategory[];
  @Output() selectedProduct = new EventEmitter<any>();

  constructor(private productService: ProductService, private toastr: ToastrService) { }

  onModalClick(event: Event): void {
    event.stopPropagation();
    this.selectedProduct.emit(this.product);
  }

  onCategoryChange(newId: number): void {
    console.log(this.product.pricingCategory)
    if(this.product.pricingCategory){
      this.product.pricingCategory.id = newId
    }else{
      this.product.pricingCategory = {id: newId} as any;
    }
    
    this.productService.updatePricingCategory(this.product.id!, newId).subscribe({
      next: () =>{
        this.toastr.success("Categoria de precificação atualizada!", "Sucesso")
      },
      error: (err) => this.toastr.error("Um erro aconteceu ao atualizar a categoria de precificação", "Erro")
    })
  }
  onToggleSync(): void {
    if (this.product.useSuggestedPrice) {
      if (this.product.suggestedPrice && this.product.suggestedPrice > 0) {
        this.product.preco = this.product.suggestedPrice;
      }else{
        this.toastr.error("Nenhum preço sugerido cadastrado para esse produto", "Erro")
        this.product.useSuggestedPrice = !this.product.useSuggestedPrice;
        return;
      } 
    }
    this.productService.updateSyncStatus(this.product.id!, this.product.useSuggestedPrice).subscribe({
      next: ()=>{
        this.toastr.success("Preço sincronizado com o preço de venda!","Sucesso");
      },
      error: (err) => {
        this.product.useSuggestedPrice = !this.product.useSuggestedPrice;
        this.toastr.error("Erro ao sincornizar o preço do produto", "Erro")
      },
    })
  }

  onToggleStatus(){
    this.productService.toggleStatus(this.product.id!).subscribe({
      next: () =>{
        this.toastr.success("Estado do produto alterado", "Sucesso");
      },
      error: () =>{
        this.product.active = !this.product.active
        this.toastr.error("Erro ao alterar estado do produto", "Erro");
      }
    })
  }

}
