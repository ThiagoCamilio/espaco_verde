import {Component, OnInit } from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {CurrencyPipe, PercentPipe, CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { TiposProdutos } from '../../models/tipos-produtos';
import { LayoutService } from '../../services/layout.service';


@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [
    FormsModule,
    CurrencyPipe,
    PercentPipe,
    CommonModule,
    RouterLink
  ],
  templateUrl: './produto-form.component.html',
  styleUrl: './produto-form.component.css'
})
export class ProdutoFormComponent implements OnInit {

  produto: Product = {
    nome: '',
    tipo: '',
    quantidade: 0,
    dataDeEntrada: '',
    precoCusto: 0,
    preco: 0,
    imagem: '',
    descricao:''
  };

  tiposProdutos = Object.values(TiposProdutos);

  successMessage: string = '';
  errorMessage: string = '';
  today: string = '';
  cartItemsCount: number = 0;
  imagePreview: string | ArrayBuffer | null = null;
  arquivoSelecionado!: File

  constructor(private produtoService: ProductService, private layoutService: LayoutService) {
    this.today = new Date().toISOString().split('T')[0];
  }

  ngOnInit(): void {
    setTimeout(() => {
      this.layoutService.updateBannerText("Cadastre novos produtos", "Adicione produtos ao seu estoque ", "Começar agora");
    });
    this.loadCartCount();
  }

  onSubmit(): void {
    this.clearMessages();
    if (!this.validateProduct()) {
      return;
    }

    this.produtoService.save(this.produto, this.arquivoSelecionado).subscribe({
      next: (res) => {
      alert('Produto salvo!');

    },
    error: (err) => alert('Erro ao salvar produto.')
    });

    this.successMessage = 'Produto cadastrado com sucesso!';
    this.resetForm();

  }

  private validateProduct(): boolean {
    if (this.produto.preco < this.produto.precoCusto) {
      this.errorMessage = 'Preço de venda não pode ser menor que o preço de custo!';
      return false;
    }
    if (this.produto.quantidade < 0) {
      this.errorMessage = 'Quantidade não pode ser negativa!';
      return false;
    }
    if (this.produto.dataDeEntrada > this.today) {
      this.errorMessage = 'Data de entrada não pode ser futura!';
      return false;
    }

    return true;
  }

  resetForm(): void {
    this.produto = {
      nome: '',
      tipo: '',
      quantidade: 0,
      dataDeEntrada: '',
      precoCusto: 0,
      preco: 0,
      imagem: '',
      descricao: ''
    };
    this.clearMessages();
  }

  private clearMessages(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }

  isFormValid(): false | "" | boolean {
    return this.produto.nome?.trim().length >= 3 &&
      this.produto.tipo &&
      this.produto.quantidade >= 0 &&
      this.produto.dataDeEntrada &&
      this.produto.dataDeEntrada <= this.today &&
      this.produto.precoCusto >= 0 &&
      this.produto.preco >= 0 &&
      this.produto.preco >= this.produto.precoCusto;
  }

  calculateProfit(): number {
    if (this.produto.preco > 0 && this.produto.precoCusto > 0) {
      return this.produto.preco - this.produto.precoCusto;
    }
    return 0;
  }

  calculateProfitMargin(): number {
    if (this.produto.precoCusto > 0 && this.produto.preco > 0) {
      return (this.produto.preco - this.produto.precoCusto) / this.produto.precoCusto;
    }
    return 0;
  }

  calculateTotalStockValue(): number {
    return this.produto.preco * this.produto.quantidade;
  }

  getTipoIcon(tipo: string): string {
    const icons: { [key: string]: string } = {
      'PLANTAS': '🌱',
      'VASOS': '🏺',
      'FERTILIZANTES': '💧',
      'FERRAMENTAS': '🔧',
      'DECORACAO': '🎨',
      'SEMENTES': '🌰'
    };
    return icons[tipo] || '📦';
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

  onLogin(): void {
    console.log('Abrir modal de login');

  }

  toggleCart(): void {
    console.log('Abrir carrinho');
  }

  onFileSelected(event:any){
    const file = event.target.files[0];
    if(file){
      this.arquivoSelecionado = file
      const reader = new FileReader();
      reader.onload = ()=>{
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }


  private loadCartCount(): void {
    // Implementar lógica para carregar itens do carrinho
    this.cartItemsCount = 0; // Exemplo
  }

  private showLoading(show: boolean): void {
    const submitBtn = document.querySelector('.btn-submit');
    if (submitBtn) {
      if (show) {
        submitBtn.classList.add('loading');
        submitBtn.setAttribute('disabled', 'true');
      } else {
        submitBtn.classList.remove('loading');
        submitBtn.removeAttribute('disabled');
      }
    }
  }

}
