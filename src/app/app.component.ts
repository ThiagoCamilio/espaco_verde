import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterLink } from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { AuthService } from './services/auth.service';
import { NotificationService } from './services/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
      RouterOutlet,
      RouterLink,
      FooterComponent,
      HeaderComponent,
      SidebarComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  
  title = 'frontend';

  constructor(private authService: AuthService, private notificationService: NotificationService){}

  ngOnInit(): void {
    if(this.authService.getToken()){
      this.notificationService.conect();
    }
  }

}
