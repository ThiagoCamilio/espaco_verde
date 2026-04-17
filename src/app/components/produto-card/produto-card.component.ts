import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { environment } from '../../../environment';
import { CommonModule, NgFor } from '@angular/common';
@Component({
  selector: 'app-produto-card',
  standalone: true,
  imports: [CommonModule, NgFor],
  templateUrl: './produto-card.component.html',
  styleUrl: './produto-card.component.css'
})
export class ProductCardComponent {
  @Input() product!: Product;

  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;

}
