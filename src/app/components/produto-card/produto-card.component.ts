import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { environment } from '../../../environment';
import { CommonModule } from '@angular/common';
import { BaseProductCardComponent } from '../base-product-card/base-product-card.component';
@Component({
  selector: 'app-produto-card',
  standalone: true,
  imports: [
    CommonModule,
    BaseProductCardComponent
  ],
  templateUrl: './produto-card.component.html',
  styleUrl: './produto-card.component.css'
})
export class ProductCardComponent {

  @Input() products!: Product[];
  @Input() product!: Product;

}
