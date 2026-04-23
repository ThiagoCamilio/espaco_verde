import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

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
}
