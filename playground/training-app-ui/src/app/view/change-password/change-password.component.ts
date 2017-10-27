import {Component, OnInit} from "@angular/core";
import {Http, RequestOptions} from "@angular/http";
import {Router} from "@angular/router";
import {ChangePassword} from "../../model/change-password";

@Component({
  selector: "app-change-password",
  templateUrl: "./change-password.component.html",
  styleUrls: ["./change-password.component.css"]
})
export class ChangePasswordComponent implements OnInit {

  changePasswordRequest: ChangePassword = new ChangePassword();
  error: boolean;
  success: boolean;

  constructor(private http: Http, private router: Router) {
  }

  ngOnInit() {
  }

  changePassword() {
    this.http.post("/account/change-password", JSON.stringify(this.changePasswordRequest),
      new RequestOptions()).subscribe(() => {
      this.success = true;
    }, () => {
      this.error = true;
    });
  }
}
