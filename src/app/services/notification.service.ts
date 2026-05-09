import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Client, Message } from '@stomp/stompjs'
import { AuthService } from './auth.service';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private badgeOrderCounter = new BehaviorSubject<number>(0);
  public badgeOrderCounter$: Observable<number> = this.badgeOrderCounter;

  private stompClient!: Client;

  constructor(private authService: AuthService, private http: HttpClient) {
    if (authService.isLoggedIn$) {
      this.loadBadgeCount();
      this.conect();
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

  private conect() {
    const token = this.authService.getToken();

    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      debug: (msg) => console.log(msg)
    });

    this.stompClient.onConnect = (frame) => {
      this.stompClient.subscribe('/topic/pending-orders', (message: Message) => {
        if (message.body) {
          const newCount = parseInt(message.body, 10);
          const oldCount = this.badgeOrderCounter.value;
          if (newCount > oldCount) {
            console.log("NOVO PEDIDO, esse log vai virar um toastr depois")
          }

          this.badgeOrderCounter.next(newCount);
        }
      });
    };
    this.stompClient.activate();
  }

  public desconect() {
    if (this.stompClient && this.stompClient.active) {
      this.stompClient.deactivate();
    }
  }
}
