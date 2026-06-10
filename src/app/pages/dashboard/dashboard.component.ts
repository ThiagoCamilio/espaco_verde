import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../components/product-card/product-card.component';
import { NgFor } from '@angular/common';
import { LayoutService } from '../../services/layout.service';
import { DashboardCardComponent } from '../../components/dashboard-card/dashboard-card.component';
import { NotificationService } from '../../services/notification.service';
import { AttendanceComponent } from '../../components/attendance/attendance.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    SidebarComponent, 
    RouterLink, 
    ProductCardComponent, 
    NgFor,
    DashboardCardComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{

  constructor(private layoutService: LayoutService, private notificationService: NotificationService){}

  ngOnInit(): void {
    this.notificationService.loadBadgeCount();
    setTimeout(() => {
      this.layoutService.updateBannerText("Bem-vindo ao seu Dashboard!", "Gerencie seu estoque e veja relatorios de suas vendas", "Comece agora");
    });
  }
}
