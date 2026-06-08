import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { CommonModule, NgClass, NgFor } from '@angular/common';
import { LayoutService } from '../../services/layout.service';

@Component({
  selector: 'app-admin-orders-list',
  standalone: true,
  imports: [
    CommonModule,
    NgClass,
    NgFor
  ],
  templateUrl: './admin-orders-list.component.html',
  styleUrl: './admin-orders-list.component.css'
})
export class AdminOrdersListComponent implements OnInit {

  orders: any[] = [];
  ordersMap: Record<string, any[]> = {};

  kanbanColumns = [
    { id: 'Aguardando Análise', title: 'Aguardando Análise', next: 'Aguardando Pagamento', cssClass: 'bg-analysis' },
    { id: 'Aguardando Pagamento', title: 'Aguardando Pgto', next: 'Pago', cssClass: 'bg-payment' },
    { id: 'Pago', title: 'Pago', next: 'Em entrega', cssClass: 'bg-paid' },
    { id: 'Em entrega', title: 'Enviado', next: 'Entregue', cssClass: 'bg-shipped' },
    { id: 'Entregue', title: 'Entregue', next: null, cssClass: 'bg-delivered' },
    { id: 'Cancelado', title: 'Cancelado', next: null, cssClass: 'bg-canceled' }
  ];

  constructor(private orderService: OrderService, private layoutService:LayoutService) { }

  ngOnInit(): void {
    setTimeout(() =>{
      this.layoutService.updateBannerText("Bem-vindo à Central de Pedidos!", "Aceite, atualize e cancele pedidos", "Comece agora");
    });
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getAllAdminOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.organizeKanbanBoard();
      },
      error: (err) => {
        console.error('Erro ao buscar pedidos', err);
      }
    });
  }

  organizeKanbanBoard(): void {
    this.kanbanColumns.forEach(col => {
      this.ordersMap[col.id] = [];
    });

    this.orders.forEach(order => {
      if (this.ordersMap[order.orderStatus]) {
        this.ordersMap[order.orderStatus].push(order);
      }
    });
  }

  promoteOrder(order: any, nextStatus: string): void {
    if (confirm(`Avançar pedido #${order.id} para a próxima etapa?`)) {
      this.updateStatus(order, nextStatus);
    }
  }

  cancelOrder(order: any): void {
    if (confirm(`Tem certeza que deseja CANCELAR o pedido #${order.id}? Essa ação devolverá o estoque.`)) {
      this.updateStatus(order, 'Cancelado');
    }
  }

  private updateStatus(order: any, newStatus: string): void {
    this.orderService.updateOrderStatus(order.id, newStatus).subscribe({
      next: (updatedOrder) => {
        const oldStatus = order.orderStatus;
        order.orderStatus = newStatus;
        this.ordersMap[oldStatus] = this.ordersMap[oldStatus].filter(o => o.id !== order.id);
        this.ordersMap[newStatus].push(order);
      },
      error: (err) => alert('Erro ao alterar o status do pedido.')
    });
  }

}
