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
      'Aguardando Análise': { label: 'Sob Análise', cssClass: 'status-analysis' },
      'Aguardando Pagamento': { label: 'Aguardando Pagamento', cssClass: 'status-payment' },
      'Pago': { label: 'Pago', cssClass: 'status-paid' },
      'Em entrega': { label: 'Enviado', cssClass: 'status-in-delivery' },
      'Entregue': { label: 'Entregue', cssClass: 'status-delivered' },
      'Cancelado': { label: 'Cancelado', cssClass: 'status-canceled' }
    };

    return statusMap[status] || { label: status, cssClass: 'status-default' };
  }

}
