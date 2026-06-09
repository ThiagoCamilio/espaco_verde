import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Client, Message, StompSubscription } from '@stomp/stompjs'
import { AuthService } from './auth.service';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly URL = `${environment.apiUrl}`;

  private badgeOrderCounter = new BehaviorSubject<number>(0);
  public badgeOrderCounter$: Observable<number> = this.badgeOrderCounter;

  private attendanceQueueTrigger = new Subject<void>();
  public attendanceQueueTrigger$ = this.attendanceQueueTrigger.asObservable();

  private stompClient!: Client;

  constructor(private authService: AuthService, private http: HttpClient, private toastrService: ToastrService) {
    if (authService.isLoggedIn$) {
      this.loadBadgeCount();
    }
  }

  private loadBadgeCount() {
    if (this.authService.getRole() === "admin") {
      this.http.get<number>(`${this.URL}/admin/orders/pending-count`)
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
    
    const socket = new SockJS(`/api/ws`);
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

        this.stompClient.subscribe('/topic/attendance-queue', (message: Message) =>{
          this.playSound();
          this.toastrService.info("Um novo cliente solicitou atendimento humano", "Atendimento!");
          this.attendanceQueueTrigger.next();
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

  public subscribeToChat(chatId: number, callback: (message: Message)=>void): StompSubscription | null{
    if(this.stompClient && this.stompClient.connected){
      return this.stompClient.subscribe(`/topic/chat/${chatId}`, callback);
    }
    return null;
  }

  private playSound(){
    const audio = new Audio('sounds/notification-bell-01.mp3')
    audio.play().catch(erro =>{
      console.warn("O navegador bloqueou o audio de notificação")
    })
  }
}
