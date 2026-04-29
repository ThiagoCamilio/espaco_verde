import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

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
export class SidebarComponent {

  @Input() isExpanded : Boolean = false;
  @Output() toggle = new EventEmitter<void>();

  constructor(private authService: AuthService, private router:Router){}

  toggleSidebar(){
    this.isExpanded = !this.isExpanded;
    this.toggle.emit();
    if(this.isExpanded == true){
      document.documentElement.style.setProperty('--sidebar-width', '160px');
    }else{
      document.documentElement.style.setProperty('--sidebar-width', '80px');
    }
  }

  logout():void{
    this.authService.logout();
    this.router.navigate(['/home'])
  }

}
