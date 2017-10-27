import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";

import {AppComponent} from "./app.component";
import {LoginComponent} from "./view/login/login.component";
import {RegisterComponent} from "./view/register/register.component";
import {MainComponent} from "./view/main/main.component";
import {FormsModule} from "@angular/forms";
import {FlexLayoutModule} from "@angular/flex-layout";
import {HttpModule, RequestOptions} from "@angular/http";
import {AppMaterialModule} from "./app.material.module";
import {RouterModule} from "@angular/router";
import {AppRoutes} from "./app.routes";
import {AppGuard} from "./app.guard";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CookieService} from "ngx-cookie-service";
import {RequestOptionsService} from "./service/request.options.service";
import {ChangePasswordComponent} from "./view/change-password/change-password.component";
import {LoginGuard} from "./login.guard";
import { EditAuthorComponent } from './view/edit-author/edit-author.component';
import { EditBookComponent } from './view/edit-book/edit-book.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    MainComponent,
    ChangePasswordComponent,
    EditAuthorComponent,
    EditBookComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    FlexLayoutModule,
    HttpModule,
    BrowserModule,
    RouterModule,
    AppMaterialModule,
    AppRoutes
  ],
  entryComponents: [EditBookComponent,EditAuthorComponent],
  providers: [AppGuard, LoginGuard, CookieService, {provide: RequestOptions, useClass: RequestOptionsService}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
