import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { NgIf } from '@angular/common';
import { UserProfileInfoComponent } from '../../components/user-profile-info/user-profile-info.component';
import { UserOrderListComponent } from '../../components/user-order-list/user-order-list.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    UserProfileInfoComponent,
    UserOrderListComponent
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {

  activeTab: 'info' | 'orders' = 'info';

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {

      if (params['tab'] === 'orders') {
        this.activeTab = 'orders';
      } else if (params['tab'] === 'info') {
        this.activeTab = 'info';
      }
    });
  }

}
