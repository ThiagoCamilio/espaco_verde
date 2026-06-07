import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OrderSummaryComponent } from '../../components/order-summary/order-summary.component';
import { Cart } from '../../models/cart';

@Component({
  selector: 'app-checkout-delivery',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    OrderSummaryComponent
  ],
  templateUrl: './checkout-delivery.component.html',
  styleUrl: './checkout-delivery.component.css'
})
export class CheckoutDeliveryComponent implements OnInit {

  cart!: Cart;
  deliveryForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private cartService: CartService, private router: Router) { }

  ngOnInit(): void {

    this.loadCart();

    this.deliveryForm = this.formBuilder.group({
      deliveryAddress: ['', Validators.required],
      deliveryMethod: ['DELIVERY', Validators.required],
      obs: ['']
    })

    this.userService.getProfile().subscribe(data => {
      this.deliveryForm.patchValue({
        deliveryAddress: data.adress
      })
    })
  }

  loadCart(): void {
    this.cartService.getMyCart().subscribe({
      next: (res) => {
        this.cart = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  proceedToConfirmation() {
    if (this.deliveryForm.invalid) {
      return;
    }
    const deliveryData = this.deliveryForm.value;
    console.log(deliveryData);
    this.router.navigate(['user/checkout/confirmation'], { state: { delivery: deliveryData } })
  }

  goBack(): void {
    this.router.navigate(['user/cart']);
  }

  get totalPrice() {
    if (!this.cart || !this.cart.productCartDTOs) return 0;
    return this.cart.price;
  }
}
