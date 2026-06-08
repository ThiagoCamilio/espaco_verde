import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Chat } from '../models/chat';
import { environment } from '../../environment';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private readonly URL = `${environment.apiUrl}/chats`;

  constructor(private http: HttpClient) { }

  getAttendanceQueue(): Observable<Chat[]>{
    return this.http.get<Chat[]>(`${this.URL}/attendance`);
  }

  getMessageHistory(chatId: number): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.URL}/${chatId}/messages`);
  }

  sendAttendantMessage(chatId: number, text:string): Observable<void>{
    return this.http.post<void>(`${this.URL}/${chatId}/send`, text)
  }

  closeAttendance(chatId: number): Observable<void>{
    return this.http.put<void>(`${this.URL}/${chatId}/close`, {})
  }


}
