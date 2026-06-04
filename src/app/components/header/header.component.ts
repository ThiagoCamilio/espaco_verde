
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

  constructor(private cartService: CartService, private router: Router, private authService: AuthService) { 
    this.totalItens$ = this.cartService.cart$.pipe(
      map(cart => {
        if(!cart || !cart.productCartDTOs) return 0;
        return cart.productCartDTOs.reduce((acc: number, item: any) => acc + item.quantity, 0);
      })
    )
  }

  ngOnInit(): void {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
    this.cartService.getMyCart().subscribe(
      {
        next:(res) =>{
          console.log(res)
        }
      }
    )
  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]);
  }

  logout():void{
    this.authService.logout();
    this.router.navigate(['/home'])
  }

  get userRole(): string | null{
    return this.authService.getRole();
  }
}
