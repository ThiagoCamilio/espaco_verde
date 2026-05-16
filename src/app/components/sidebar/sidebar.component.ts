import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [ 
    CommonModule,
    RouterModule,
    RouterLink
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{

  @Input() isExpanded : Boolean = false;
  @Output() toggle = new EventEmitter<void>();

  orderCount: number = 0;
  private sub!: Subscription;

  constructor(private authService: AuthService, private router:Router, private notificationService: NotificationService){}

  ngOnInit(): void {
      this.sub = this.notificationService.badgeOrderCounter$.subscribe({
        next:(count) =>{
          this.orderCount = count;
        }
      })
  }

  toggleSidebar(){
    this.isExpanded = !this.isExpanded;
    this.toggle.emit();
    if(this.isExpanded == true){
      document.documentElement.style.setProperty('--sidebar-width', '170px');
    }else{
      document.documentElement.style.setProperty('--sidebar-width', '80px');
    }
  }

  logout():void{
    this.authService.logout();
    this.router.navigate(['/home'])
  }

}
