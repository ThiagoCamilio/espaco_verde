import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginResponse } from '../models/login-response';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly URL = `${environment.apiUrl}/auth`;

  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  isLoggedIn$ = this.loggedIn.asObservable();

  constructor(private http:HttpClient) { }

  private hasToken(): boolean{
    return !!sessionStorage.getItem('auth-token');
  }

  getToken(): string | null{
    return sessionStorage.getItem('auth-token');
  }
  
  getRole(): string | null{

    const token = this.getToken();

    if(!token){
      return null
    }
    const decodedToken:any = jwtDecode(token)
    return decodedToken.role;

  }

  registerUser(userToRegister:any):Observable<any>{
    return this.http.post(`${this.URL}/register`, userToRegister);
  }

  login(login: string, password:string){
    return this.http.post<LoginResponse>(`${this.URL}/login`,{login, password}).pipe(
      tap(value => {
        if(value.token){
          sessionStorage.setItem('auth-token', value.token)
          this.loggedIn.next(true)
          console.log(this.loggedIn)
        }
      })
    )
  }

  logout():void{
    sessionStorage.removeItem('auth-token');
    this.loggedIn.next(false);
  }
}
