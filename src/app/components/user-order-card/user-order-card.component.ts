import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Order } from '../../models/order';

@Component({
  selector: 'app-user-order-card',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './user-order-card.component.html',
  styleUrl: './user-order-card.component.css'
})
export class UserOrderCardComponent {

  @Input() order!: Order;
  @Input() route: string = '';


  getStatusInfo(status: string): { label: string, cssClass: string } {
    const statusMap: Record<string, { label: string, cssClass: string }> = {
      'AWAITING_ANALYSIS': { label: 'Sob Análise', cssClass: 'status-analysis' },
      'AWAITING_PAYMENT': { label: 'Aguardando Pagamento', cssClass: 'status-payment' },
      'PAID': { label: 'Pago', cssClass: 'status-paid' },
      'IN_DELIVERY': { label: 'Enviado', cssClass: 'status-in-delivery' },
      'DELIVERED': { label: 'Entregue', cssClass: 'status-delivered' },
      'CANCELED': { label: 'Cancelado', cssClass: 'status-canceled' }
    };

    return statusMap[status] || { label: status, cssClass: 'status-default' };
  }

}
