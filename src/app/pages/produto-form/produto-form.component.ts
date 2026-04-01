import { Component, OnInit } from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {CurrencyPipe, PercentPipe} from '@angular/common';

export enum TiposProdutos {
  PLANTAS = 'PLANTAS',
  VASOS = 'VASOS',
  FERTILIZANTES = 'FERTILIZANTES',
  FERRAMENTAS = 'FERRAMENTAS',
  DECORACAO = 'DECORACAO',
  SEMENTES = 'SEMENTES'
}

export interface Produto {
  id?: number;
  nome: string;
  tipo: TiposProdutos | string;
  quantidade: number;
  dataDeEntrada: string;
  precoCusto: number;
  preco: number;
}

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [
    FormsModule,
    CurrencyPipe,
    PercentPipe
  ],
  templateUrl: './produto-form.component.html',
  styleUrl: './produto-form.component.css'
})
export class ProdutoFormComponent implements OnInit {

  produto: Produto = {
    nome: '',
    tipo: '',
    quantidade: 0,
    dataDeEntrada: '',
    precoCusto: 0,
    preco: 0
  };

  tiposProdutos = Object.values(TiposProdutos);

  successMessage: string = '';
  errorMessage: string = '';
  today: string = '';
  cartItemsCount: number = 0;

  constructor() {
    this.today = new Date().toISOString().split('T')[0];
  }

  ngOnInit(): void {
    this.loadCartCount();
  }

  onSubmit(): void {
    this.clearMessages();
    if (!this.validateProduct()) {
      return;
    }

    // Simula loading
    this.showLoading(true);

    // Simula envio para API
    setTimeout(() => {
      console.log('Produto cadastrado:', this.produto);

      // this.produtoService.save(this.produto).subscribe(
      //   (response) => {
      //     this.successMessage = 'Produto cadastrado com sucesso!';
      //     this.resetForm();
      //   },
      //   (error) => {
      //     this.errorMessage = 'Erro ao cadastrar produto. Tente novamente.';
      //   }
      // );

      // Simula sucesso
      this.successMessage = 'Produto cadastrado com sucesso!';
      this.resetForm();
      this.showLoading(false);

      setTimeout(() => {
        this.successMessage = '';
      }, 3000);
    }, 1000);
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
      preco: 0
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
