import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { BaseProductCardComponent } from '../base-product-card/base-product-card.component';
import { NgFor } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-storage-product-card',
  standalone: true,
  imports: [
    BaseProductCardComponent,
    NgFor,
    RouterLink
  ],
  templateUrl: './storage-product-card.component.html',
  styleUrl: './storage-product-card.component.css'
})
export class StorageProductCardComponent {
  @Input() products!: Product[];
  @Input() product!: Product;
}
