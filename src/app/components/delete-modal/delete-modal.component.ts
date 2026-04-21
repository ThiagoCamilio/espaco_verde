import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-delete-modal',
  standalone: true,
  imports: [],
  templateUrl: './delete-modal.component.html',
  styleUrl: './delete-modal.component.css'
})
export class DeleteModalComponent {

  constructor(private router: Router, private productService: ProductService){}

  @Input() product!: Product;

  @Output() closeModal = new EventEmitter<void>();
  @Output() confirmExclude = new EventEmitter<void>();
  @Output() excludeSucess = new EventEmitter<void>();

  close(){
    this.closeModal.emit();
  }

  exclude(){
    this.productService.delete(this.product.id!).subscribe({
      next:(res) =>{
        this.close();
        this.router.navigate(['admin/estoque'])
        this.excludeSucess.emit();

      },
      error:(err) => {
        alert("Algo deu errado na exclusão")
        this.router.navigate(['admin/estoque'])
      }
    });
  }

}
