import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { BannerComponent } from '../../components/banner/banner.component';
import { BannerService } from '../../services/banner.service';
import { CommonModule, NgIf } from '@angular/common';
import { AttendanceComponent } from '../../components/attendance/attendance.component';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    SidebarComponent,
    DashboardComponent,
    BannerComponent,
    NgIf,
    CommonModule,
    RouterLinkActive,
    AttendanceComponent
  ],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {

  constructor(public bannerService: BannerService){}

  isSidebarOpen : Boolean = false;

  toggleSidebar (){
    this.isSidebarOpen = !this.isSidebarOpen;
  }

}
