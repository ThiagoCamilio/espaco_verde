import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { environment } from '../../../environment';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-produto-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './produto-card.component.html',
  styleUrl: './produto-card.component.css'
})
export class ProductCardComponent {
  @Input() product!: Product;

  baseImageUrl = `${environment.apiUrl}/produtos/imagem/`;

}
