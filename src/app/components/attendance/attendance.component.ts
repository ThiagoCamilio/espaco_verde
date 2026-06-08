import { Component, OnDestroy, OnInit } from '@angular/core';
import { Chat } from '../../models/chat';
import { Message } from '../../models/message';
import { StompSubscription } from '@stomp/stompjs';
import { ChatService } from '../../services/chat.service';
import { NotificationService } from '../../services/notification.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './attendance.component.html',
  styleUrl: './attendance.component.css'
})
export class AttendanceComponent implements OnInit, OnDestroy {

  attendanceQueue: Chat[] = [];
  currentMessages: Message[] = [];
  activeChatId: number | null = null;
  newMessage: string = '';
  activeChatState: string | null = null;

  private chatSubscription: StompSubscription | null = null;

  constructor(
    private chatService: ChatService,
    private notificationService: NotificationService,
    private toastr: ToastrService
  ) {

  }

  ngOnInit(): void {
    this.loadQueue();
    this.notificationService.attendanceQueueTrigger$.subscribe(() => {
      this.loadQueue();
    })
  }

  loadQueue(): void {


    this.chatService.getAttendanceQueue().subscribe({
      next: (queue) => {
        this.attendanceQueue = queue.sort((a, b) => {
          const aIsHuman = a.chatState === 'IN_HUMAN_ATTENDANCE';
          const bIsHuman = b.chatState === 'IN_HUMAN_ATTENDANCE';
          if (aIsHuman && !bIsHuman) return -1
          if (!aIsHuman && bIsHuman) return 1;

          return new Date(b.lastInteractionAt).getTime() - new Date(a.lastInteractionAt).getTime();
        });

        if (this.activeChatId) {
          const currentActiveChat = this.attendanceQueue.find(c => c.id === this.activeChatId);
          if (currentActiveChat) {
            this.activeChatState = currentActiveChat.chatState
          }
        }
      }
    }

    );
  }

  openChat(chatId: number): void {
    this.activeChatId = chatId;

    const clickedChat = this.attendanceQueue.find(c => c.id === chatId);
    this.activeChatState = clickedChat ? clickedChat.chatState : null;
    this.chatService.getMessageHistory(chatId).subscribe(history => {
      this.currentMessages = history
      this.scrollToBottom();
    }
    )

    if (this.chatSubscription) {
      this.chatSubscription.unsubscribe();
    }

    this.chatSubscription = this.notificationService.subscribeToChat(chatId, (msgRaw) => {
      const newMessage: Message = JSON.parse(msgRaw.body);
      this.currentMessages.push(newMessage);
      this.scrollToBottom();
    })
  }

  sendMessage(): void {
    if (!this.newMessage.trim() || !this.activeChatId) return;

    const text = this.newMessage;
    this.newMessage = '';
    const sentMessage: Message = {
      id: Date.now(),
      content: text,
      senderType: 'SYSTEM',
      timestamp: new Date().toISOString()
    }

    this.currentMessages.push(sentMessage);
    this.scrollToBottom();

    this.chatService.sendAttendantMessage(this.activeChatId, text).subscribe({
      error: (err) => this.toastr.error("Erro ao enviar mensage", "Erro!")
    })
  }

  closeChat(): void {
    if (!this.activeChatId) return;

    this.chatService.closeAttendance(this.activeChatId).subscribe(() => {
      this.activeChatId = null;
      this.currentMessages = [];
      this.loadQueue();
      if (this.chatSubscription) this.chatSubscription.unsubscribe();
    });
  }

  scrollToBottom(): void {
    setTimeout(() => {
      const container = document.getElementById('chat-scroll-area');
      if (container) container.scrollTop = container.scrollHeight;
    }, 100);
  }

  ngOnDestroy(): void {
    if (this.chatSubscription) this.chatSubscription.unsubscribe();
  }

}
