import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { interval, Subscription, switchMap, takeWhile } from 'rxjs';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-user-order-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './user-order-details.component.html',
  styleUrl: './user-order-details.component.css'
})
export class UserOrderDetailsComponent implements OnInit {

  orderId: string | null = null;
  order!: any;
  orderStatus!: string;
  private orderSub!: Subscription;

  constructor(
    private orderService: OrderService,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
    private notificationService: NotificationService
  ) {

  }

  ngOnInit(): void {
    this.orderId = this.route.snapshot.paramMap.get('id');
    this.orderService.getOrderById(this.orderId!).subscribe({
      next: (data) => {
        this.order = data
        this.orderStatus = this.order.orderStatus;
      },
      error: (err) => this.toastr.error(err, "Erro")
    });

    this.paymentVerification();

  }

  paymentVerification(): void {
    this.orderSub = this.notificationService.orderUpdate$.subscribe({
        next: (order) => {
          this.order = order
          if (this.orderStatus !== order.orderStatus) {
            this.orderStatus = order.orderStatus;
            this.cdr.detectChanges();
            if (this.orderStatus === "PAID") {
              this.toastr.success("Seu pedido logo será preparado para envio", "Pagamento confirmado!")
            } else if (this.orderStatus === "IN_DELIVERY") {
              this.toastr.success("Seu pedido saiu para entrega", "Atenção!")
            } else if (this.orderStatus === "DELIVERED") {
              this.toastr.success("Seu pedido foi entregue !!", "Sucesso")
            } else if (this.orderStatus === "CANCELED") {
              this.toastr.error("Seu pedido foi cancelado ou o pix expirou", "Ops!")
            }
          }
        },
        error: (err) => {
          this.toastr.error("Erro ao consultar o status do seu pedido", "Ops!")
        }
      })
  }

  copyPix() {
    const pix = this.order.payment.copyAndPastCode;
    navigator.clipboard.writeText(pix).then(() => {
      this.toastr.success("Pix copia e cola copiado para a Área de Transferencia", "Sucesso")
    })
  }

  ngOnDestroy(): void {
    if (this.orderSub) {
      this.orderSub.unsubscribe();
    }
  }

}
