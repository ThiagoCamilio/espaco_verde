
import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CommonModule, NgIf } from '@angular/common';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  imports:[
    RouterLink,
    CommonModule
  ],
  standalone: true,
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  cartItemCount: number = 0;

isLoggedIn$!: Observable<boolean>;

  constructor(private cartService: CartService, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  toggleCart(): void {
    this.cartService.toggleCart();
  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]);
  }

  logout():void{
    this.authService.logout();
    this.router.navigate(['/home'])
  }
}
