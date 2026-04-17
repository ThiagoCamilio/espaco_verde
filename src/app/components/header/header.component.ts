
import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  imports:[RouterLink],
  standalone: true,
  styleUrls: ['./header.component.css']
})
export class HeaderComponent{
  cartItemCount: number = 0;

  constructor(private cartService: CartService, private router: Router) { }


  toggleCart(): void {
    this.cartService.toggleCart();
  }

  
  navigateTo(route: string) {
    this.router.navigate([`/${route}`]);
  }
}
