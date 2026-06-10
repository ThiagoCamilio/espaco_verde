import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { CommonModule, NgFor } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UserOrderCardComponent } from '../user-order-card/user-order-card.component';
import { Order } from '../../models/order';

@Component({
  selector: 'app-user-order-list',
  standalone: true,
  imports: [
    CommonModule,
    NgFor,
    RouterLink,
    UserOrderCardComponent
  ],
  templateUrl: './user-order-list.component.html',
  styleUrl: './user-order-list.component.css'
})


export class UserOrderListComponent implements OnInit {

  orders: Order[] = []

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getMyOrders().subscribe({
      next: (data: Order[]) => {
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
      'Aguardando Análise':  { label: 'Sob Análise', cssClass: 'status-analysis' },
      'Aguardando Pagamento':   { label: 'Aguardando Pagamento', cssClass: 'status-payment' },
      'Pago':               { label: 'Pago', cssClass: 'status-paid' },
      'Em entrega':        { label: 'Enviado', cssClass: 'status-in-delivery' },
      'Entregue':          { label: 'Entregue', cssClass: 'status-delivered' },
      'Cancelado':           { label: 'Cancelado', cssClass: 'status-canceled' }
    };

    return statusMap[status] || { label: status, cssClass: 'status-default' };
  }

}
