// Imports

import {ModuleWithProviders} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AppGuard} from "./app.guard";
import {MainComponent} from "./view/main/main.component";
import {LoginComponent} from "./view/login/login.component";
import {RegisterComponent} from "./view/register/register.component";
import {ChangePasswordComponent} from "./view/change-password/change-password.component";
import {LoginGuard} from "./login.guard";


// Route Configuration
export const routes: Routes = [
  {
    path: "",
    component: MainComponent,
    canActivate: [AppGuard]
  },
  {
    path: "uilogin",
    component: LoginComponent,
    canActivate: [LoginGuard]
  },
  {
    path: "register",
    component: RegisterComponent,
    canActivate: [LoginGuard]
  },
  {
    path: "change-password",
    component: ChangePasswordComponent,
    canActivate: [AppGuard]
  }


];
export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes, {useHash: false});
