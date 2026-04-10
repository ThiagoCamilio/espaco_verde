import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginResponse } from '../models/login-response';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private readonly URL = 'http://localhost:8080/auth'

  constructor(private httpClient: HttpClient) { }
  login(login: string, password:string){
    return this.httpClient.post<LoginResponse>(`${this.URL}/login`,{login, password}).pipe(
      tap((value) => {
        sessionStorage.setItem("auth-token", value.token)
      })
    )
  }
}
