import { Injectable } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivateChild {

  constructor(private router: Router, private authService: AuthService) {}

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const authToken = this.authService.getToken();
    if (!authToken) {
      this.router.navigate(['/login']);
      return false;
    } 

    const expectedRole = next.data['expectedRole'] || next.parent?.data['expectedRole']

    const userRole = this.authService.getRole();

    if(expectedRole && userRole !== expectedRole ){
      this.router.navigate(['/home']);
      return false;
    }

    return true; 
    
  }
}