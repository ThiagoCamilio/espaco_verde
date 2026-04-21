import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product';
import { BaseProductCardComponent } from '../base-product-card/base-product-card.component';
import { NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DeleteModalComponent } from '../delete-modal/delete-modal.component';

@Component({
  selector: 'app-storage-product-card',
  standalone: true,
  imports: [
    BaseProductCardComponent,
    NgFor,
    RouterLink,
    DeleteModalComponent,
    NgIf
  ],
  templateUrl: './storage-product-card.component.html',
  styleUrl: './storage-product-card.component.css'
})
export class StorageProductCardComponent {
  @Input() products!: Product[];
  @Input() product!: Product;
  @Output() selectedProduct = new EventEmitter<any>();

  onModalClick(event: Event):void{
    event.stopPropagation();
    this.selectedProduct.emit(this.product);
  }

}
