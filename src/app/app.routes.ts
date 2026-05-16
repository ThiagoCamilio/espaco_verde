import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProductFormComponent } from './pages/product-form/product-form.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { StorageComponent } from './pages/storage/storage.component';
import { StorageProductDetailsComponent } from './pages/storage-product-details/storage-product-details.component';
import { AuthGuard } from './services/auth-guard.service';
import { RegisterFormComponent } from './pages/register-form/register-form.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { ProductDetailsComponent } from './pages/product-details/product-details.component';
import { CartComponent } from './pages/cart/cart.component';
import { CheckoutDeliveryComponent } from './pages/checkout-delivery/checkout-delivery.component';
import { CheckoutConfirmationComponent } from './pages/checkout-confirmation/checkout-confirmation.component';
import { CheckoutSuccessComponent } from './pages/checkout-success/checkout-success.component';
import { AdminOrdersListComponent } from './pages/admin-orders-list/admin-orders-list.component';
import { PricingDashboardComponent } from './pages/pricing-dashboard/pricing-dashboard.component';

export const routes: Routes = [
  {
    path :'',
    component: UserLayoutComponent,
    children:[
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'login', component: LoginFormComponent },
      { path: 'register', component: RegisterFormComponent },
      { path: 'product/:id', component: ProductDetailsComponent}

    ]
  },

  { path: 'admin',
    component: AdminLayoutComponent,
    canActivateChild:[AuthGuard],
    data: { expectedRole: 'admin' },
    children:[
      { path: '', redirectTo: '/admin/dashboard', pathMatch: 'full'},
      { path: 'dashboard', component: DashboardComponent},
      { path: 'cadastro', component: ProductFormComponent},
      { path: 'estoque', component: StorageComponent},
      { path: 'estoque/product/:id', component: StorageProductDetailsComponent},
      { path: 'orders', component: AdminOrdersListComponent},
      { path: 'pricing', component: PricingDashboardComponent}

    ],   
  },

  {
    path: 'user',
    component: UserLayoutComponent,
    canActivateChild:[AuthGuard],
    data: {expectedRole: 'user'},
    children:[
      { path: '', redirectTo: '/user/profile', pathMatch:'full'},
      { path: 'profile', component: UserProfileComponent},
      { path: 'cart', component: CartComponent},
      { path: 'checkout/delivery', component: CheckoutDeliveryComponent},
      { path: 'checkout/confirmation', component: CheckoutConfirmationComponent},
      { path: 'checkout/success', component: CheckoutSuccessComponent}


    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
