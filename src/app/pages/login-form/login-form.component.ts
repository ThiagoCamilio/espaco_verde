import { Component, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import { LoginService } from '../../services/login.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule
  ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css',
  providers:[
    LoginService
  ]
})
export class LoginFormComponent{

  user: User ={
    login: "",
    password: ""
  }

  constructor(private loginService: LoginService){
  }
  

  onSubmit():void{
    
    this.loginService.login(this.user.login, this.user.password).subscribe({
      next: () => console.log("logou"),
      error: () => console.log("nao logou")
    })
  }

}
