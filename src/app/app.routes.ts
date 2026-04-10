import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatalogComponent } from './pages/catalag/catalag.component';
import { ProdutoFormComponent } from './pages/produto-form/produto-form.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { UserComponent } from './pages/user/user.component';
import { AuthGuard } from './services/auth-guard.service';

export const routes: Routes = [
  { path: '', redirectTo: '/catalog', pathMatch: 'full' },
  { path: 'catalog', component: CatalogComponent },
  { path: 'cadastro', component: ProdutoFormComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'user', component: UserComponent, canActivate:[AuthGuard] }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
