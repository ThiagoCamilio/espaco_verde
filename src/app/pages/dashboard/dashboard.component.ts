import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../components/produto-card/produto-card.component';
import { NgFor } from '@angular/common';
import { Product } from '../../models/product';
import { BannerComponent } from '../../components/banner/banner.component';
import { LayoutService } from '../../services/layout.service';
import { DashboardCardComponent } from '../../components/dashboard-card/dashboard-card.component';

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

  constructor(private layoutService: LayoutService){}

  ngOnInit(): void {
    setTimeout(() => {
      this.layoutService.updateBannerText("Bem-vindo ao seu Dashboard!", "Gerencie seu estoque e veja relatorios de suas vendas", "Comece agora");
    });
  }
}
