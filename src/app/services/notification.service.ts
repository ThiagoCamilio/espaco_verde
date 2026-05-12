import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Client, Message } from '@stomp/stompjs'
import { AuthService } from './auth.service';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private badgeOrderCounter = new BehaviorSubject<number>(0);
  public badgeOrderCounter$: Observable<number> = this.badgeOrderCounter;

  private stompClient!: Client;

  constructor(private authService: AuthService, private http: HttpClient, private toastrService: ToastrService) {
    if (authService.isLoggedIn$) {
      this.loadBadgeCount();
    }
  }

  private loadBadgeCount() {
    if (this.authService.getRole() === "admin") {
      this.http.get<number>('http://localhost:8080/admin/orders/pending-count')
        .subscribe({
          next: (count) => {
            this.badgeOrderCounter.next(count);
          },
          error: (err) => console.error('Erro ao carregar contagem inicial', err)
        });
    }
  }

  public conect() {
    const token = this.authService.getToken();
    

    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      connectHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    this.stompClient.onConnect = (frame) => {
      if (this.authService.getRole() === 'admin') {
        this.stompClient.subscribe('/topic/pending-orders', (message: Message) => {
          if (message.body) {
            const newCount = parseInt(message.body, 10);
            const oldCount = this.badgeOrderCounter.value;
            if (newCount > oldCount) {
              this.playSound();
              this.toastrService.warning("Um novo pedido esta aguardando sua análise", "Novo pedido!")
            }

            this.badgeOrderCounter.next(newCount);
          }
        });
      }

      this.stompClient.subscribe('/user/queue/order-updates', (message: Message)=>{
        if(message.body){
          this.playSound();
          this.toastrService.info(message.body, "Atualização do Pedido")
        }
      })

    };
    this.stompClient.activate();
  }

  public desconect() {
    if (this.stompClient && this.stompClient.active) {
      this.stompClient.deactivate();
    }
  }

  private playSound(){
    const audio = new Audio('sounds/notification-bell-01.mp3')
    audio.play().catch(erro =>{
      console.warn("O navegador bloqueou o audio de notificação")
    })
  }
}
