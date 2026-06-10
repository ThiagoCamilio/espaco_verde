import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  constructor() { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
    const token = sessionStorage.getItem("auth-token")

    let header = req.headers.set('ngrok-skip-browser-warning', 'true')

    if(token){
      const request = req.clone({
        setHeaders:{
          Authorization: `Bearer ${token}`
        }
      })
      return next.handle(request)
    }

    const request = req.clone({
      setHeaders:{
        'ngrok-skip-browser-warning':'True'
      }
    })
    
    return next.handle(request)
  }
}
