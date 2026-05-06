import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout-success',
  standalone: true,
  imports: [],
  templateUrl: './checkout-success.component.html',
  styleUrl: './checkout-success.component.css'
})
export class CheckoutSuccessComponent implements OnInit {

  orderId: string | number = '';

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state || history.state;

    if (state && state.orderId) {
      this.orderId = state.orderId;
    }
  }

  ngOnInit(): void {
    if (!this.orderId) {
      this.router.navigate(['/']);
    }
  }

  goToMyOrders(): void {
    this.router.navigate(['/user/profile'], { queryParams: { tab: 'orders' } });
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }

}
