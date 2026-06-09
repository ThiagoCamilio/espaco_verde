import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, PercentPipe, CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { TiposProdutos } from '../../models/tipos-produtos';
import { LayoutService } from '../../services/layout.service';


@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    FormsModule,
    CurrencyPipe,
    PercentPipe,
    CommonModule,
    RouterLink
  ],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {

  product: any = {
    nome: '',
    tipo: '',
    stockQuantity: 0,
    precoCusto: 0,
    preco: 0,
    descricao: ''
  };

  tiposProdutos = Object.values(TiposProdutos);

  successMessage: string = '';
  errorMessage: string = '';
  cartItemsCount: number = 0;
  imagePreview: string | ArrayBuffer | null = null;
  arquivoSelecionado!: File

  constructor(private produtoService: ProductService, private layoutService: LayoutService) {  }

  ngOnInit(): void {
    setTimeout(() => {
      this.layoutService.updateBannerText("Cadastre novos produtos", "Adicione produtos ao seu estoque ", "Começar agora");
    });
  }

  onSubmit(): void {
    this.clearMessages();
    if (!this.validateProduct()) {
      return;
    }

    this.produtoService.save(this.product, this.arquivoSelecionado).subscribe({
      next: (res) => {
        alert('Produto salvo!');

      },
      error: (err) => alert()
    });

    this.successMessage = 'Produto cadastrado com sucesso!';
    this.resetForm();

  }

  private validateProduct(): boolean {
    if (this.product.preco < this.product.precoCusto) {
      this.errorMessage = 'Preço de venda não pode ser menor que o preço de custo!';
      return false;
    }
    if (this.product.stockQuantity < 0) {
      this.errorMessage = 'Quantidade não pode ser negativa!';
      return false;
    }

    return true;
  }

  resetForm(): void {
    this.product = {
      nome: '',
      tipo: '',
      stockQuantity: 0,
      precoCusto: 0,
      preco: 0,
      descricao: ''
    };
    this.clearMessages();
  }

  private clearMessages(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }

  isFormValid(): false | "" | boolean {
    return this.product.nome?.trim().length >= 3 &&
      this.product.tipo &&
      this.product.stockQuantity >= 0 &&
      this.product.precoCusto >= 0 &&
      this.product.preco >= 0 &&
      this.product.preco >= this.product.precoCusto;
  }

  calculateProfit(): number {
    if (this.product.preco > 0 && this.product.precoCusto > 0) {
      return this.product.preco - this.product.precoCusto;
    }
    return 0;
  }

  calculateProfitMargin(): number {
    if (this.product.precoCusto > 0 && this.product.preco > 0) {
      return (this.product.preco - this.product.precoCusto) / this.product.precoCusto;
    }
    return 0;
  }

  calculateTotalStockValue(): number {
    return this.product.preco * this.product.stockQuantity;
  }

  getTipoDescricao(tipo: string): string {
    const descricoes: { [key: string]: string } = {
      'PLANTAS': 'Plantas',
      'VASOS': 'Vasos',
      'FERTILIZANTES': 'Fertilizantes',
      'FERRAMENTAS': 'Ferramentas',
      'DECORACAO': 'Decoração',
      'SEMENTES': 'Sementes'
    };
    return descricoes[tipo] || tipo;
  }

  scrollToForm(): void {
    const element = document.getElementById('form-container');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }


  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.arquivoSelecionado = file
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

}
