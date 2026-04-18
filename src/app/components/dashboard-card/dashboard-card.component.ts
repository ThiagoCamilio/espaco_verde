import { Component, Input } from '@angular/core';
import { DashboardCard } from '../../models/dashboard-card';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard-card',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './dashboard-card.component.html',
  styleUrl: './dashboard-card.component.css'
})
export class DashboardCardComponent {

  @Input() card!: DashboardCard;
  @Input() title: string = '';
  @Input() background: string = '';
  @Input() route: string = '';

}
