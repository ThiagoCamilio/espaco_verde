import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-checkout-delivery',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './checkout-delivery.component.html',
  styleUrl: './checkout-delivery.component.css'
})
export class CheckoutDeliveryComponent implements OnInit{

  deliveryForm!: FormGroup;
  totalValue: number = 0; 

  constructor(private formBuilder: FormBuilder, private userService: UserService, private cartService:CartService, private router:Router){}

  ngOnInit(): void {
    this.totalValue = this.cartService.totalPrice;

    this.deliveryForm = this.formBuilder.group({
      adress:['', Validators.required],
      deliveryMethod:['Retirada', Validators.required],
      obs:['']
    })

    this.userService.getProfile().subscribe(data =>{
      this.deliveryForm.patchValue({
        adress: data.adress
      })
    })
  }

  proceedToPayment(){
    if(this.deliveryForm.invalid){
      return;
    }
    const deliveryData = this.deliveryForm.value;
    console.log(deliveryData);
    this.router.navigate(['/checkout/payment'])

  }

}
