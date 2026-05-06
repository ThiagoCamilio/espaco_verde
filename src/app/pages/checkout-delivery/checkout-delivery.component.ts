import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OrderSummaryComponent } from '../../components/order-summary/order-summary.component';

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

  deliveryForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private cartService: CartService, private router: Router) { }

  ngOnInit(): void {

    this.deliveryForm = this.formBuilder.group({
      adress: ['', Validators.required],
      deliveryMethod: ['PICKUP', Validators.required],
      obs: ['']
    })

    this.userService.getProfile().subscribe(data => {
      this.deliveryForm.patchValue({
        adress: data.adress
      })
    })
  }

  proceedToConfirmation() {
    if (this.deliveryForm.invalid) {
      return;
    }
    const deliveryData = this.deliveryForm.value;
    this.router.navigate(['user/checkout/confirmation'], { state: { delivery: deliveryData } })

  }

  goBack(): void {
    this.router.navigate(['user/cart']);
  }

  get totalPrice() {
    return this.cartService.totalPrice
  }
}
