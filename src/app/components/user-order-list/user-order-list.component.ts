import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { CommonModule, NgFor } from '@angular/common';
import { RouterLink } from '@angular/router';


export interface OrderItem {
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}

export interface OrderResponse {
  id: number;
  createdAt: string;
  orderStatus: string;
  totalPrice: number;
  items: OrderItem[];
  deliveryMethod: String;
  deliveryAdress: String;
}

@Component({
  selector: 'app-user-order-list',
  standalone: true,
  imports: [
    CommonModule,
    NgFor,
    RouterLink
  ],
  templateUrl: './user-order-list.component.html',
  styleUrl: './user-order-list.component.css'
})


export class UserOrderListComponent implements OnInit {



  orders: OrderResponse[] = []

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getMyOrders().subscribe({
      next: (data: OrderResponse[]) => {
        this.orders = data.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      },
      error: (err) => {
        console.log("erro ao carregar pedidos", err);
        alert("erro ao carregar pedidos");
      }
    })
  }

  getStatusInfo(status: string): { label: string, cssClass: string } {
    const statusMap: Record<string, { label: string, cssClass: string }> = {
      'AWAITING_ANALYSIS':  { label: 'Sob Análise', cssClass: 'status-analysis' },
      'AWAITING_PAYMENT':   { label: 'Aguardando Pagamento', cssClass: 'status-payment' },
      'PAID':               { label: 'Pago', cssClass: 'status-paid' },
      'IN_DELIVERY':        { label: 'Enviado', cssClass: 'status-in-delivery' },
      'DELIVERED':          { label: 'Entregue', cssClass: 'status-delivered' },
      'CANCELED':           { label: 'Cancelado', cssClass: 'status-canceled' }
    };

    return statusMap[status] || { label: status, cssClass: 'status-default' };
  }

}
