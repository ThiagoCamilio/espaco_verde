import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProdutoFormComponent } from './pages/produto-form/produto-form.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { UserComponent } from './pages/user/user.component';
import { AuthGuard } from './services/auth-guard.service';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

export const routes: Routes = [
  /*{ path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'cadastro', component: ProdutoFormComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'user', component: UserComponent, canActivate:[AuthGuard] },
  { path: 'dashboard', component:DashboardComponent},*/

  {
    path :'',
    component: UserLayoutComponent,
    children:[
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'login', component: LoginFormComponent },
    ]
  },

  { path: 'admin',
    component: AdminLayoutComponent,
    children:[
      { path: '', redirectTo: '/admin/dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent},
      { path: 'cadastro', component: ProdutoFormComponent },

    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
