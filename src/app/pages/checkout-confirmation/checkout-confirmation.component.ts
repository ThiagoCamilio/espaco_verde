import { Component, OnInit } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OrderSummaryComponent } from '../../components/order-summary/order-summary.component';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-checkout-confirmation',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    OrderSummaryComponent
  ],
  templateUrl: './checkout-confirmation.component.html',
  styleUrl: './checkout-confirmation.component.css'
})
export class CheckoutConfirmationComponent implements OnInit{

  items$: Observable<CartItem[]>; 
  deliveryData : any;
  isSubmiting: boolean = false;

  constructor(private cartService: CartService, private orderService: OrderService, private router: Router){
    
    this.items$ = this.cartService.items$;
    const navigation = this.router.getCurrentNavigation();
    if(navigation?.extras.state){   
      this.deliveryData = navigation.extras.state['delivery']
    }else{
      this.deliveryData = history.state.delivery;
    }

  }

  ngOnInit(): void {
    if(!this.deliveryData){
      this.router.navigate(['user/checkout/delivery'])
    }
  }

  get totalPrice(){
    return this.cartService.totalPrice
  }

  async confirmOrder(): Promise<void>{
    this.isSubmiting = true;

    try {
      
      const currentItems = await firstValueFrom(this.cartService.items$);

      
      if (!currentItems || currentItems.length === 0) {
        alert('Seu carrinho está vazio.');
        this.isSubmiting = false;
        return;
      }


    const orderRequest = {
      deliveryAdress : this.deliveryData.adress,
      deliveryMethod : this.deliveryData.deliveryMethod,
      items: currentItems.map(item => ({
          productId: item.productId,
          quantity: item.quantity
        }))
    };

    this.orderService.createOrder(orderRequest).subscribe({
      next: (response) => {
        this.cartService.clean();
        this.router.navigate(['user/checkout/success'], {state:{orderId: response.id}});
      },
      error: (err) => {
        console.error('Erro ao processar pedido:', err);
        this.isSubmiting = false;
        alert('Ocorreu um problema ao enviar seu pedido.');
      }
    });
    } catch (error) {
      console.error('Erro ao acessar o carrinho:', error);
      this.isSubmiting = false;
    }
  }

  goBack(): void {
    this.router.navigate(['user/checkout/delivery']);
  }
}
