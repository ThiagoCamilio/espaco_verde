import { Component, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import { User } from '../../models/user';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule
  ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css',

})
export class LoginFormComponent{

  user: User ={
    login: "",
    password: ""
  }

  constructor(private authService: AuthService, private router: Router){
  }
  
  onSubmit():void{
    
    this.authService.login(this.user.login, this.user.password).subscribe({
      next: () => this.router.navigate(['/home']),
      error: () => this.router.navigate(['/login'])
    })
  }

}
