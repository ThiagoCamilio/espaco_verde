
import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router, RouterLink } from '@angular/router';
import { map, Observable } from 'rxjs';
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
  
  totalItens$!: Observable<number>;
  isLoggedIn$!: Observable<boolean>;

  constructor(private cartService: CartService, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
    this.totalItens$ = this.cartService.items$.pipe(map(items => items.reduce ((acc, item)=> acc + item.quantity, 0)))
  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]);
  }

  logout():void{
    this.authService.logout();
    this.router.navigate(['/home'])
  }
}
