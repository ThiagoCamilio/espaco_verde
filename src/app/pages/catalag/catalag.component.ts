import { Component } from '@angular/core';
import { CurrencyPipe, NgFor } from '@angular/common';
import { RouterLink, Router } from '@angular/router';

@Component({
  selector: 'app-catalog',
  standalone: true,
  templateUrl: './catalag.component.html',
  styleUrls: ['./catalag.component.css'] ,
  imports: [CurrencyPipe, NgFor, RouterLink]
})
export class CatalogComponent {
  products = [
    {
      id: 1,
      name: 'Rosas Vermelhas',
      category: 'Flores',
      price: 89.90,
      description: 'Lindo buquê com 12 rosas vermelhas frescas.',
      image: 'https://images.unsplash.com/photo-1563241527-3004b7be0ffd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8cm9zZXN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60'
    },
    {
      id: 2,
      name: 'Kit Suculentas',
      category: 'Plantas',
      price: 45.00,
      description: 'Kit com 3 lindas suculentas em vasos decorativos.',
      image: 'https://images.unsplash.com/photo-1597848212624-e9f912c19b6f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8c3VjY3VsZW50fGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60'
    },
    {
      id: 3,
      name: 'Arranjo Primavera',
      category: 'Arranjos',
      price: 120.00,
      description: 'Belíssimo arranjo com flores da estação.',
      image: 'https://images.unsplash.com/photo-1519378058457-4c29a0a2efac?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Zmxvd2VyJTIwYXJyYW5nZW1lbnR8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60'
    },
    {
      id: 4,
      name: 'Orquídea Phalaenopsis',
      category: 'Plantas',
      price: 75.00,
      description: 'Linda orquídea phalaenopsis branca em vaso decorativo.',
      image: 'https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8b3JjaGlkfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60'
    },
    {
      id: 5,
      name: 'Cesta de Flores',
      category: 'Presentes',
      price: 135.00,
      description: 'Linda cesta com flores variadas e chocolate.',
      image: 'https://images.unsplash.com/photo-1581993192008-63fd1ea7de1a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8Zmxvd2VyJTIwYmFza2V0fGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60'
    },
    {
      id: 6,
      name: 'Buquê de Girassóis',
      category: 'Flores',
      price: 65.00,
      description: 'Buquê vibrante com 10 girassóis frescos.',
      image: 'https://images.unsplash.com/photo-1596461404969-9ae70f2830c1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8c3VuZmxvd2VyfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60'
    }
  ];

  activeFilter: string = 'Todas';

  constructor(private router: Router) {}

  filterProducts(category: string) {
    this.activeFilter = category;
  }

  addToCart(product: any) {
    alert(`${product.name} adicionado ao carrinho!`);

  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]); // Navega para a rota
  }
}
