import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProdutoFormComponent } from './pages/produto-form/produto-form.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { StorageComponent } from './pages/storage/storage.component';
import { StorageProductDetailsComponent } from './pages/storage-product-details/storage-product-details.component';
import { AuthGuard } from './services/auth-guard.service';
import { RegisterFormComponent } from './pages/register-form/register-form.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';

export const routes: Routes = [
  {
    path :'',
    component: UserLayoutComponent,
    children:[
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'login', component: LoginFormComponent },
      { path: 'register', component: RegisterFormComponent },

    ]
  },

  { path: 'admin',
    component: AdminLayoutComponent,
    canActivateChild:[AuthGuard],
    data: { expectedRole: 'admin' },
    children:[
      { path: '', redirectTo: '/admin/dashboard', pathMatch: 'full'},
      { path: 'dashboard', component: DashboardComponent},
      { path: 'cadastro', component: ProdutoFormComponent},
      { path: 'estoque', component: StorageComponent},
      { path: 'estoque/produto/:id', component: StorageProductDetailsComponent}
    ],   
  },

  {
    path: 'user',
    component: UserLayoutComponent,
    canActivateChild:[AuthGuard],
    data: {expectedRole: 'user'},
    children:[
      { path: '', redirectTo: '/user/profile', pathMatch:'full'},
      { path: 'profile', component: UserProfileComponent}
    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
