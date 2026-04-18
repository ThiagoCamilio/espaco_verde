import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { environment } from '../../../environment';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-base-product-card',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './base-product-card.component.html',
  styleUrl: './base-product-card.component.css'
})
export class BaseProductCardComponent {
  @Input() product!: Product;
  @Input() route: string = '';

  
  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;
}
