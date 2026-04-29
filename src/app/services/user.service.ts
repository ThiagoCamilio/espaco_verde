import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly URL = `${environment.apiUrl}/user`;

  constructor(private http:HttpClient) { }

  getProfile(): Observable<any>{
    return this.http.get(`${this.URL}/profile`);
  }

  updateProfile(data:any): Observable<any>{
    return this.http.put(`${this.URL}/profile`, data);
  }
}
