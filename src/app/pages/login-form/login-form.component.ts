import { Component, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import { User } from '../../models/user';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';
import { BannerComponent } from '../../components/banner/banner.component';
import { LayoutService } from '../../services/layout.service';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    BannerComponent
  ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css',

})
export class LoginFormComponent implements OnInit{

  user: User ={
    login: "",
    password: ""
  }

  constructor(private authService: AuthService, private router: Router, private notificationService: NotificationService, public layoutService: LayoutService,){
  }
  
ngOnInit(): void {
      setTimeout(()=>{
      this.layoutService.updateBannerText("Bem-vindo à Espaço Verde!","Descubra nossa coleção de plantas e flores para todos os momentos especiais", "Veja nossas ofertas");
    })
}

  onSubmit():void{
    
    this.authService.login(this.user.login, this.user.password).subscribe({
      next: () => {
        this.router.navigate(['/home'])
        this.notificationService.conect();
      },
      error: () => this.router.navigate(['/login'])
    })
  }

}
